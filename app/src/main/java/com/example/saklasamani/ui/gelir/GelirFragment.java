package com.example.saklasamani.ui.gelir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.saklasamani.R;

import java.util.ArrayList;

public class GelirFragment extends Fragment {

    private TextView tvIncome, tvTotalIncome, tvTotalExtra;
    private EditText etAmount, etNote, etMainIncome;
    private Button btnAddExtraToggle, btnConfirmExtra, btnUpdateIncome;
    private ListView lvExtraIncomes;
    private LinearLayout extraInputLayout;

    private GelirViewModel gelirViewModel;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> incomeNotes = new ArrayList<>();

    private boolean isExtraInputVisible = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gelir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvIncome = view.findViewById(R.id.tvIncome);
        tvTotalExtra = view.findViewById(R.id.tvTotalExtra);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        etAmount = view.findViewById(R.id.etExtraAmount);
        etNote = view.findViewById(R.id.etExtraNote);
        btnAddExtraToggle = view.findViewById(R.id.btnAddExtra);
        btnConfirmExtra = view.findViewById(R.id.btnConfirmExtra);
        btnUpdateIncome = view.findViewById(R.id.btnUpdateIncome);
        etMainIncome = view.findViewById(R.id.etMainIncome);
        extraInputLayout = view.findViewById(R.id.extraInputLayout);
        lvExtraIncomes = view.findViewById(R.id.lvExtraIncomes);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, incomeNotes);
        lvExtraIncomes.setAdapter(adapter);

        gelirViewModel = new ViewModelProvider(this).get(GelirViewModel.class);
        gelirViewModel.init(requireContext());

        // LiveData observer'ları
        gelirViewModel.getIncome().observe(getViewLifecycleOwner(), income -> {
            tvIncome.setText("Gelir: " + income + "₺");
            etMainIncome.setText(String.valueOf(income));
        });

        gelirViewModel.getExtraIncomes().observe(getViewLifecycleOwner(), extraIncomes -> {
            incomeNotes.clear();
            double totalExtra = 0;
            for (var ei : extraIncomes) {
                incomeNotes.add(ei.getAmount() + "₺ - " + ei.getNote());
                totalExtra += ei.getAmount();
            }
            adapter.notifyDataSetChanged();
            tvTotalExtra.setText("Ek Gelir Toplamı: " + totalExtra + "₺");
        });

        gelirViewModel.getTotalIncome().observe(getViewLifecycleOwner(), totalIncome -> {
            tvTotalIncome.setText("Toplam Gelir: " + totalIncome + "₺");
        });

        btnUpdateIncome.setOnClickListener(v -> {
            String incomeStr = etMainIncome.getText().toString().trim();
            if (!incomeStr.isEmpty()) {
                double newIncome = Double.parseDouble(incomeStr);
                gelirViewModel.updateIncome(newIncome);
                Toast.makeText(requireContext(), "Gelir ve bütçe güncellendi.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Gelir boş olamaz.", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddExtraToggle.setOnClickListener(v -> {
            isExtraInputVisible = !isExtraInputVisible;
            extraInputLayout.setVisibility(isExtraInputVisible ? View.VISIBLE : View.GONE);
        });

        btnConfirmExtra.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            String note = etNote.getText().toString().trim();

            if (amountStr.isEmpty() || note.isEmpty()) {
                Toast.makeText(requireContext(), "Tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            gelirViewModel.addExtraIncome(amount, note);

            etAmount.setText("");
            etNote.setText("");
        });

        lvExtraIncomes.setOnItemClickListener((parent, view1, position, id) -> {
            gelirViewModel.deleteExtraIncome(position);
            Toast.makeText(requireContext(), "Silindi", Toast.LENGTH_SHORT).show();
        });
    }
}
