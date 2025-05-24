package com.example.saklasamani.ui.gelir;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.saklasamani.data.ExtraIncomeDao;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.ExtraIncome;
import com.example.saklasamani.model.User;

import java.util.List;

public class GelirViewModel extends ViewModel {

    private UserDao userDao;
    private ExtraIncomeDao extraIncomeDao;
    private User currentUser;

    private final MutableLiveData<Double> income = new MutableLiveData<>();
    private final MutableLiveData<List<ExtraIncome>> extraIncomes = new MutableLiveData<>();
    private final MutableLiveData<Double> totalIncome = new MutableLiveData<>();

    public void init(Context context) {
        userDao = new UserDao(context);
        extraIncomeDao = new ExtraIncomeDao(context);
        currentUser = SessionManager.getInstance().getUser();

        loadData();
    }

    private void loadData() {
        if (currentUser == null) return;

        income.setValue(currentUser.getIncome());
        List<ExtraIncome> extras = extraIncomeDao.getExtraIncomes(currentUser.getUserName());
        extraIncomes.setValue(extras);
        calculateTotalIncome(extras, currentUser.getIncome());
    }

    private void calculateTotalIncome(List<ExtraIncome> extras, double mainIncome) {
        double totalExtra = 0;
        for (ExtraIncome ei : extras) {
            totalExtra += ei.getAmount();
        }
        totalIncome.setValue(mainIncome + totalExtra);
    }

    public LiveData<Double> getIncome() {
        return income;
    }

    public LiveData<List<ExtraIncome>> getExtraIncomes() {
        return extraIncomes;
    }

    public LiveData<Double> getTotalIncome() {
        return totalIncome;
    }

    public void updateIncome(double newIncome) {
        if (currentUser == null) return;

        double oldIncome = currentUser.getIncome();
        currentUser.setIncome(newIncome);
        userDao.updateIncome(currentUser.getUserName(), newIncome);

        double difference = newIncome - oldIncome;
        if (difference > 0) {
            userDao.increaseBudget(currentUser.getUserName(), difference);
        } else if (difference < 0) {
            userDao.decreaseBudget(currentUser.getUserName(), -difference);
        }

        currentUser = userDao.getUserByUserName(currentUser.getUserName());
        SessionManager.getInstance().setUser(currentUser);

        income.setValue(newIncome);
        calculateTotalIncome(extraIncomes.getValue(), newIncome);
    }

    public void addExtraIncome(double amount, String note) {
        if (currentUser == null) return;

        extraIncomeDao.addExtraIncome(currentUser.getUserName(), amount, note);

        List<ExtraIncome> updatedList = extraIncomeDao.getExtraIncomes(currentUser.getUserName());
        extraIncomes.setValue(updatedList);

        userDao.increaseBudget(currentUser.getUserName(), amount);

        currentUser = userDao.getUserByUserName(currentUser.getUserName());
        SessionManager.getInstance().setUser(currentUser);

        income.setValue(currentUser.getIncome());
        calculateTotalIncome(updatedList, currentUser.getIncome());
    }

    public void deleteExtraIncome(int position) {
        if (currentUser == null) return;

        List<ExtraIncome> currentExtras = extraIncomes.getValue();
        if (currentExtras == null || position < 0 || position >= currentExtras.size()) return;

        ExtraIncome toRemove = currentExtras.get(position);
        boolean deleted = extraIncomeDao.deleteExtraIncome(currentUser.getUserName(), toRemove.getNote());

        if (deleted) {
            userDao.decreaseBudget(currentUser.getUserName(), toRemove.getAmount());

            List<ExtraIncome> updatedList = extraIncomeDao.getExtraIncomes(currentUser.getUserName());
            extraIncomes.setValue(updatedList);

            currentUser = userDao.getUserByUserName(currentUser.getUserName());
            SessionManager.getInstance().setUser(currentUser);

            income.setValue(currentUser.getIncome());
            calculateTotalIncome(updatedList, currentUser.getIncome());
        }
    }
}
