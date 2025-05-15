package com.example.saklasamani.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.User;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Double> budget = new MutableLiveData<>();

    public LiveData<Double> getBudget() {
        return budget;
    }

    public void loadUserData() {
        User user = SessionManager.getInstance().getUser();
        if (user != null) {
            budget.setValue(user.getBudget());
        }
    }

}