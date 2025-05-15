package com.example.saklasamani.ui.harcama;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.saklasamani.data.HarcamaDao;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.Harcama;
import com.example.saklasamani.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class HarcamaViewModel extends AndroidViewModel {

    private HarcamaDao harcamaDao;
    private UserDao userDao;

    private MutableLiveData<List<Harcama>> harcamalar = new MutableLiveData<>();
    private MutableLiveData<Double> toplamHarcama = new MutableLiveData<>(0.0);
    private MutableLiveData<Map<String, Double>> kategoriBazliHarcama = new MutableLiveData<>();

    private MutableLiveData<String> selectedKategori = new MutableLiveData<>("Tümü");
    private LiveData<Double> kategoriHarcamasi;

    public HarcamaViewModel(@NonNull Application application) {
        super(application);
        harcamaDao = new HarcamaDao(application);
        userDao = new UserDao(application);

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

    public void getHarcamalarAsync(String userName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = harcamaDao.getHarcamalar(userName);
            harcamalar.postValue(list);

            double toplam = 0;
            Map<String, Double> kategoriMap = new HashMap<>();

            for (Harcama h : list) {
                toplam += h.getAmount();
                String kategori = h.getCategory();
                kategoriMap.put(kategori, kategoriMap.getOrDefault(kategori, 0.0) + h.getAmount());
            }

            toplamHarcama.postValue(toplam);
            kategoriBazliHarcama.postValue(kategoriMap);
        });
    }

    public void addHarcama(String userName, double amount, String category, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            harcamaDao.addHarcama(userName, amount, category, note);

            List<Harcama> updatedList = harcamaDao.getHarcamalar(userName);
            harcamalar.postValue(updatedList);

            double totalSpent = 0;
            Map<String, Double> kategoriMap = new HashMap<>();

            for (Harcama h : updatedList) {
                totalSpent += h.getAmount();
                String kategori = h.getCategory();
                kategoriMap.put(kategori, kategoriMap.getOrDefault(kategori, 0.0) + h.getAmount());
            }

            toplamHarcama.postValue(totalSpent);
            kategoriBazliHarcama.postValue(kategoriMap);

            User user = userDao.getUserByUserName(userName);
            if (user != null) {
                double startingBudget = user.getBudget();
                double newBudget = startingBudget - amount;
                userDao.updateBudget(userName, newBudget);
                User updatedUser = userDao.getUserByUserName(userName);
                SessionManager.getInstance().setUser(updatedUser);
            }
        });
    }

    public LiveData<Double> getKategoriHarcamasi(String kategori) {
        MutableLiveData<Double> kategoriHarcama = new MutableLiveData<>(0.0);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = harcamaDao.getHarcamalar(SessionManager.getInstance().getUser().getUserName());

            double toplam = 0;
            for (Harcama h : list) {
                if (h.getCategory().equals(kategori)) {
                    toplam += h.getAmount();
                }
            }

            kategoriHarcama.postValue(toplam);
        });

        return kategoriHarcama;
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
            harcamaDao.deleteHarcama(userName, note);
            getHarcamalarAsync(userName);
        });
    }
}
