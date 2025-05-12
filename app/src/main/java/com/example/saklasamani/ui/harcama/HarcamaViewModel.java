package com.example.saklasamani.ui.harcama;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.saklasamani.data.HarcamaDao;
import com.example.saklasamani.model.Harcama;

import java.util.List;
import java.util.concurrent.Executors;

public class HarcamaViewModel extends AndroidViewModel {

    private HarcamaDao harcamaDao;
    private MutableLiveData<List<Harcama>> harcamalar = new MutableLiveData<>();

    public HarcamaViewModel(@NonNull Application application) {
        super(application);
        harcamaDao = new HarcamaDao(application);
    }

    public LiveData<List<Harcama>> getHarcamalarLiveData() {
        return harcamalar;
    }

    public void getHarcamalarAsync(String userName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Harcama> list = harcamaDao.getHarcamalar(userName);
            harcamalar.postValue(list);
        });
    }

    public void addHarcama(String userName, double amount, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            harcamaDao.addHarcama(userName, amount, note);
            getHarcamalarAsync(userName);
        });
    }

    public void deleteHarcama(String userName, String note) {
        Executors.newSingleThreadExecutor().execute(() -> {
            harcamaDao.deleteHarcama(userName, note);
            getHarcamalarAsync(userName);
        });
    }
}
