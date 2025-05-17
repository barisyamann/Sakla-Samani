package com.example.saklasamani.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.saklasamani.R;
import com.example.saklasamani.data.ExtraIncomeDao;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.model.User;
import com.example.saklasamani.manager.SessionManager;

import java.util.List;
import com.example.saklasamani.model.ExtraIncome;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView tvIncome, tvBudget, tvExtraIncome;
    private UserDao userDao;
    private ExtraIncomeDao extraIncomeDao;

    private User currentUser;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userDao = new UserDao(getContext());
        extraIncomeDao = new ExtraIncomeDao(getContext());

        tvIncome = root.findViewById(R.id.tvIncome);
        tvBudget = root.findViewById(R.id.tvBudget);
        tvExtraIncome = root.findViewById(R.id.tvExtraIncome); // Layout'ta bu id olmalı

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        currentUser = SessionManager.getInstance().getUser();

        updateIncomeViews();
        updateBudgetView();
        updateExtraIncomeView();

        homeViewModel.getBudget().observe(getViewLifecycleOwner(), budget -> {
            if (budget != null) {
                tvBudget.setText("Bütçe: " + budget + "₺");
            }
        });

        homeViewModel.loadUserData(); // Verileri yüklüyoruz

        return root;
    }

    private void updateIncomeViews() {
        if (currentUser != null) {
            double income = currentUser.getIncome();
            tvIncome.setText("Gelir: " + income + "₺");
        }
    }

    private void updateBudgetView() {
        if (currentUser != null) {
            double budget = currentUser.getBudget();
            tvBudget.setText("Bütçe: " + budget + "₺");
        }
    }

    private void updateExtraIncomeView() {
        if (currentUser != null) {
            List<ExtraIncome> extraIncomes = extraIncomeDao.getExtraIncomes(currentUser.getUserName());
            double totalExtra = 0;
            for (ExtraIncome ei : extraIncomes) {
                totalExtra += ei.getAmount();
            }
            tvExtraIncome.setText("Toplam Ek Gelir: " + totalExtra + "₺");
        }
    }
}
