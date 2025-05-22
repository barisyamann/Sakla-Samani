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

    private MutableLiveData<List<Harcama>> harcamalar = new MutableLiveData<>();
    private MutableLiveData<Double> toplamHarcama = new MutableLiveData<>(0.0);
    private MutableLiveData<Map<String, Double>> kategoriBazliHarcama = new MutableLiveData<>();

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
                kategoriMap.put(h.getCategory(), kategoriMap.getOrDefault(h.getCategory(), 0.0) + h.getAmount());
            }
            toplamHarcama.postValue(toplam);
            kategoriBazliHarcama.postValue(kategoriMap);
        });
    }

    public void addHarcama(String userName, double amount, String category, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            harcamaDao.addHarcama(userName, amount, category, note);
            userDao.decreaseBudget(userName, amount);
            updateSessionUser(userName);
            getHarcamalarAsync(userName);
        });
    }

    public void deleteHarcama(String userName, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = harcamaDao.getHarcamalar(userName);
            for (Harcama h : list) {
                if (h.getNote().equals(note)) {
                    harcamaDao.deleteHarcama(userName, note);
                    userDao.increaseBudget(userName, h.getAmount());
                    updateSessionUser(userName);
                    break;
                }
            }
            getHarcamalarAsync(userName);
        });
    }

    private void updateSessionUser(String userName) {
        User updatedUser = userDao.getUserByUserName(userName);
        if (updatedUser != null) {
            SessionManager.getInstance().setUser(updatedUser);
        }
    }
}
