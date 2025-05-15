package com.example.saklasamani.ui.harcama;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.saklasamani.data.HarcamaDao;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.Harcama;
import com.example.saklasamani.model.User;

import java.util.List;
import java.util.concurrent.Executors;

public class HarcamaViewModel extends AndroidViewModel {

    private HarcamaDao harcamaDao;
    private UserDao userDao;
    private MutableLiveData<List<Harcama>> harcamalar = new MutableLiveData<>();
    private MutableLiveData<Double> toplamHarcama = new MutableLiveData<>(0.0);

    public HarcamaViewModel(@NonNull Application application) {
        super(application);
        harcamaDao = new HarcamaDao(application);
        userDao = new UserDao(application);
    }

    public LiveData<List<Harcama>> getHarcamalarLiveData() {
        return harcamalar;
    }

    public LiveData<Double> getToplamHarcama() {
        return toplamHarcama;
    }

    public void getHarcamalarAsync(String userName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = harcamaDao.getHarcamalar(userName);
            harcamalar.postValue(list);

            double toplam = 0;
            for (Harcama h : list) {
                toplam += h.getAmount();
            }
            toplamHarcama.postValue(toplam);
        });
    }

    public void addHarcama(String userName, double amount, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {

            // Harcamayı ekle
            harcamaDao.addHarcama(userName, amount, note);

            // Güncel harcama listesini al
            List<Harcama> updatedList = harcamaDao.getHarcamalar(userName);
            harcamalar.postValue(updatedList);

            // Harcamaların toplamını hesapla
            double totalSpent = 0;
            for (Harcama h : updatedList) {
                totalSpent += h.getAmount();
            }
            toplamHarcama.postValue(totalSpent);

            // Kullanıcının gelirini çek (başlangıç bütçesi gibi düşün)
            User user = userDao.getUserByUserName(userName);
            if (user != null) {
                double startingBudget = user.getBudget(); // veya başlangıç bütçesi
                double newBudget = startingBudget - amount;

                // Bütçeyi güncelle
                userDao.updateBudget(userName, newBudget);

                User updatedUser = userDao.getUserByUserName(userName);

                // Session'daki kullanıcıyı güncelle
                SessionManager.getInstance().setUser(updatedUser);
            }
        });
    }

    public void deleteHarcama(String userName, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            harcamaDao.deleteHarcama(userName, note);
            getHarcamalarAsync(userName);
        });
    }
}
