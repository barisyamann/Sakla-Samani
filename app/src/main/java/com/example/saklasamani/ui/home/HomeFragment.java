package com.example.saklasamani.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saklasamani.MainActivity;
import com.example.saklasamani.R;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.model.ExtraIncome;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private ExtraIncomeAdapter adapter;
    private TextView tvIncome, tvTotalExtraIncome, tvBudget;
    private Button btnAddExtraIncome;
    private UserDao userDao;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userDao = new UserDao(getContext());

        recyclerView = root.findViewById(R.id.recyclerView);
        tvIncome = root.findViewById(R.id.tvIncome);
        tvTotalExtraIncome = root.findViewById(R.id.tvTotalExtraIncome);
        tvBudget=root.findViewById(R.id.tvBudget);
        btnAddExtraIncome = root.findViewById(R.id.btnAddExtraIncome);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        if (MainActivity.currentUser.getExtraIncomes() == null)
            MainActivity.currentUser.setExtraIncomes(new ArrayList<>());

        adapter = new ExtraIncomeAdapter(MainActivity.currentUser.getExtraIncomes(), position -> {
            ExtraIncome removed = MainActivity.currentUser.getExtraIncomes().remove(position);
            adapter.notifyItemRemoved(position);
            userDao.deleteExtraIncome(MainActivity.currentUser.getUserName(), removed.getNote());
            updateTotalExtraIncome();
        });

        recyclerView.setAdapter(adapter);
        updateIncomeViews();

        btnAddExtraIncome.setOnClickListener(view -> showAddIncomeDialog());

        homeViewModel.getBudget().observe(getViewLifecycleOwner(), budget -> {
            if (budget != null) {
                tvBudget.setText("Bütçe: " + budget + "₺");
            }
        });

        homeViewModel.loadUserData();


        return root;
    }

    private void showAddIncomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Yeni Ek Gelir");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputAmount = new EditText(getContext());
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputAmount.setHint("Tutar ₺");
        layout.addView(inputAmount);

        final EditText inputNote = new EditText(getContext());
        inputNote.setHint("Not");
        layout.addView(inputNote);

        builder.setView(layout);

        builder.setPositiveButton("Ekle", (dialog, which) -> {
            double amount = Double.parseDouble(inputAmount.getText().toString());
            String note = inputNote.getText().toString();

            ExtraIncome income = new ExtraIncome(amount, note);
            MainActivity.currentUser.getExtraIncomes().add(income);
            userDao.addExtraIncome(MainActivity.currentUser.getUserName(), amount, note);
            adapter.notifyItemInserted(MainActivity.currentUser.getExtraIncomes().size() - 1);
            updateTotalExtraIncome();
        });

        builder.setNegativeButton("İptal", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateIncomeViews() {
        double income = MainActivity.currentUser.getIncome();
        tvIncome.setText("Gelir: " + income + "₺");
        updateTotalExtraIncome();
    }

    private void updateTotalExtraIncome() {
        double totalExtra = 0;
        for (ExtraIncome ei : MainActivity.currentUser.getExtraIncomes()) {
            totalExtra += ei.getAmount();
        }
        tvTotalExtraIncome.setText("Toplam Ek Gelir: " + totalExtra + "₺");
    }
}
