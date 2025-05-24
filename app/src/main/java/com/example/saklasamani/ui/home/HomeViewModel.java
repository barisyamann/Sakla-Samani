package com.example.saklasamani.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.saklasamani.data.ExtraIncomeDao;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.model.ExtraIncome;
import com.example.saklasamani.model.User;
import com.example.saklasamani.manager.SessionManager;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Double> budget = new MutableLiveData<>();
    private final MutableLiveData<Double> income = new MutableLiveData<>();
    private final MutableLiveData<Double> totalExtraIncome = new MutableLiveData<>();

    private UserDao userDao;
    private ExtraIncomeDao extraIncomeDao;
    private User currentUser;

    public void init(UserDao userDao, ExtraIncomeDao extraIncomeDao) {
        this.userDao = userDao;
        this.extraIncomeDao = extraIncomeDao;
        currentUser = SessionManager.getInstance().getUser();

        loadUserData();
    }

    public LiveData<Double> getBudget() {
        return budget;
    }

    public LiveData<Double> getIncome() {
        return income;
    }

    public LiveData<Double> getTotalExtraIncome() {
        return totalExtraIncome;
    }

    public void loadUserData() {
        if (currentUser != null) {
            income.setValue(currentUser.getIncome());
            budget.setValue(currentUser.getBudget());

            List<ExtraIncome> extraIncomes = extraIncomeDao.getExtraIncomes(currentUser.getUserName());
            double totalExtra = 0;
            for (ExtraIncome ei : extraIncomes) {
                totalExtra += ei.getAmount();
            }
            totalExtraIncome.setValue(totalExtra);
        }
    }
}
