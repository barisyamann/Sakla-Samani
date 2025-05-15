package com.example.saklasamani.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.saklasamani.R;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.model.User;
import com.example.saklasamani.manager.SessionManager;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView tvIncome, tvBudget;
    private UserDao userDao;


    User currentUser = SessionManager.getInstance().getUser();
    public HomeFragment(

    ) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userDao = new UserDao(getContext());

        tvIncome = root.findViewById(R.id.tvIncome);
        tvBudget = root.findViewById(R.id.tvBudget);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        updateIncomeViews();
        updateBudgetView();

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
}
