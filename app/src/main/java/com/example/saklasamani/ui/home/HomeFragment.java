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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView tvIncome, tvBudget, tvExtraIncome;
    private UserDao userDao;
    private ExtraIncomeDao extraIncomeDao;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // View tanımlamaları
        tvIncome = root.findViewById(R.id.tvIncome);
        tvBudget = root.findViewById(R.id.tvBudget);
        tvExtraIncome = root.findViewById(R.id.tvExtraIncome);

        // DAO nesneleri oluşturuluyor
        userDao = new UserDao(getContext());
        extraIncomeDao = new ExtraIncomeDao(getContext());

        // ViewModelProvider ile ViewModel alınıyor ve DAO'lar init ediliyor
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init(userDao, extraIncomeDao);

        // LiveData gözlemleri (observe) ile UI güncelleniyor
        homeViewModel.getIncome().observe(getViewLifecycleOwner(), income -> {
            if (income != null) {
                tvIncome.setText("Gelir: " + income + "₺");
            }
        });

        homeViewModel.getBudget().observe(getViewLifecycleOwner(), budget -> {
            if (budget != null) {
                tvBudget.setText("Bütçe: " + budget + "₺");
            }
        });

        homeViewModel.getTotalExtraIncome().observe(getViewLifecycleOwner(), totalExtra -> {
            if (totalExtra != null) {
                tvExtraIncome.setText("Toplam Ek Gelir: " + totalExtra + "₺");
            }
        });

        return root;
    }
}
