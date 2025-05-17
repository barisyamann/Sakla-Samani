package com.example.saklasamani.ui.harcama;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.saklasamani.data.ExtraIncomeDao;
import com.example.saklasamani.data.HarcamaDao;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.ExtraIncome;
import com.example.saklasamani.model.Harcama;
import com.example.saklasamani.model.User;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class HarcamaViewModel extends AndroidViewModel {

    private HarcamaDao harcamaDao;
    private UserDao userDao;
    private ExtraIncomeDao extraIncomeDao;

    private MutableLiveData<List<Harcama>> harcamalar = new MutableLiveData<>();
    private MutableLiveData<Double> toplamHarcama = new MutableLiveData<>(0.0);
    private MutableLiveData<Map<String, Double>> kategoriBazliHarcama = new MutableLiveData<>();

    private MutableLiveData<String> selectedKategori = new MutableLiveData<>("Tümü");
    private LiveData<Double> kategoriHarcamasi;

    public HarcamaViewModel(@NonNull Application application) {
        super(application);
        harcamaDao = new HarcamaDao(application);
        userDao = new UserDao(application);
        extraIncomeDao = new ExtraIncomeDao(application);

        kategoriHarcamasi = Transformations.switchMap(selectedKategori, kategori -> {
            MutableLiveData<Double> result = new MutableLiveData<>(0.0);

            Executors.newSingleThreadExecutor().execute(() -> {
                User currentUser = SessionManager.getInstance().getUser();
                if (currentUser == null) {
                    result.postValue(0.0);
                    return;
                }
                List<Harcama> list = harcamaDao.getHarcamalar(currentUser.getUserName());
                double toplam = 0;
                for (Harcama h : list) {
                    if (kategori.equals("Tümü") || h.getCategory().equals(kategori)) {
                        toplam += h.getAmount();
                    }
                }
                result.postValue(toplam);
            });

            return result;
        });
    }

    public void setSelectedKategori(String kategori) {
        selectedKategori.postValue(kategori);
    }

    public LiveData<Double> getKategoriHarcamasi() {
        return kategoriHarcamasi;
    }

    public LiveData<List<Harcama>> getHarcamalarLiveData() {
        return harcamalar;
    }

    public LiveData<Double> getToplamHarcama() {
        return toplamHarcama;
    }

    public LiveData<Map<String, Double>> getKategoriBazliHarcama() {
        return kategoriBazliHarcama;
    }

    private void updateToplamlarVeKullaniciBütçe(String userName) {
        List<Harcama> list = harcamaDao.getHarcamalar(userName);
        double toplamHarcamaLocal = 0;
        Map<String, Double> kategoriMap = new HashMap<>();

        for (Harcama h : list) {
            toplamHarcamaLocal += h.getAmount();
            String kategori = h.getCategory();
            kategoriMap.put(kategori, kategoriMap.getOrDefault(kategori, 0.0) + h.getAmount());
        }

        toplamHarcama.postValue(toplamHarcamaLocal);
        kategoriBazliHarcama.postValue(kategoriMap);

        // ExtraIncome toplamı
        List<ExtraIncome> extraIncomes = extraIncomeDao.getExtraIncomes(userName);
        double extraIncomeTotal = 0;
        for (ExtraIncome e : extraIncomes) {
            extraIncomeTotal += e.getAmount();
        }

        User user = userDao.getUserByUserName(userName);
        if (user != null) {
            double newBudget = user.getIncome() + extraIncomeTotal - toplamHarcamaLocal;
            userDao.updateBudget(userName, newBudget);

            User updatedUser = userDao.getUserByUserName(userName);
            SessionManager.getInstance().setUser(updatedUser);
        }
    }

    public void getHarcamalarAsync(String userName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = harcamaDao.getHarcamalar(userName);
            harcamalar.postValue(list);
            updateToplamlarVeKullaniciBütçe(userName);
        });
    }

    public void addHarcama(String userName, double amount, String category, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            harcamaDao.addHarcama(userName, amount, category, note);
            List<Harcama> updatedList = harcamaDao.getHarcamalar(userName);
            harcamalar.postValue(updatedList);

            updateToplamlarVeKullaniciBütçe(userName);
        });
    }

    public void getHarcamalarByKategori(String userName, String kategori) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = harcamaDao.getHarcamalar(userName);
            double toplam = 0;
            List<Harcama> filtrelenmis = new ArrayList<>();

            for (Harcama h : list) {
                if (kategori.equals("Tümü") || h.getCategory().equals(kategori)) {
                    filtrelenmis.add(h);
                    toplam += h.getAmount();
                }
            }

            harcamalar.postValue(filtrelenmis);
            toplamHarcama.postValue(toplam);
        });
    }

    public void deleteHarcama(String userName, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> harcamalarUser = harcamaDao.getHarcamalar(userName);

            Harcama silinecekHarcama = null;
            for (Harcama h : harcamalarUser) {
                if (h.getNote().equals(note)) {
                    silinecekHarcama = h;
                    break;
                }
            }

            if (silinecekHarcama != null) {
                harcamaDao.deleteHarcama(userName, note);

                User user = userDao.getUserByUserName(userName);
                if (user != null) {
                    Log.d("DeleteHarcama", "Budget before increase: " + user.getBudget());
                    boolean result = userDao.increaseBudget(userName, silinecekHarcama.getAmount());
                    Log.d("DeleteHarcama", "Budget increase result: " + result);

                    User updatedUser = userDao.getUserByUserName(userName);
                    Log.d("DeleteHarcama", "Budget after increase: " + updatedUser.getBudget());
                    SessionManager.getInstance().setUser(updatedUser);
                }
                updateToplamlarVeKullaniciBütçe(userName);

                List<Harcama> updatedList = harcamaDao.getHarcamalar(userName);
                harcamalar.postValue(updatedList);
            }
        });
    }
}
