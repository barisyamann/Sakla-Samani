package com.example.saklasamani.ui.harcama;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.model.Harcama;

import java.util.List;
import java.util.concurrent.Executors;

public class HarcamaViewModel extends AndroidViewModel {

    private UserDao userDao;
    private MutableLiveData<List<Harcama>> harcamalar = new MutableLiveData<>();

    public HarcamaViewModel(@NonNull Application application) {
        super(application);
        userDao = new UserDao(application);
    }

    public LiveData<List<Harcama>> getHarcamalarLiveData() {

        return harcamalar;
    }

    public void getHarcamalarAsync(String userName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = userDao.getHarcamalar(userName);
            harcamalar.postValue(list);
        });
    }

    public void addHarcama(String userName, double amount, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDao.addHarcama(userName, amount, note);
            getHarcamalarAsync(userName); // listeyi güncelle
        });
    }

    public void deleteHarcama(String userName, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDao.deleteHarcama(userName, note);
            getHarcamalarAsync(userName); // listeyi güncelle
        });
    }
}

