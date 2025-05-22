package com.example.saklasamani.ui.harcama;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.saklasamani.R;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.User;

public class HarcamaFragment extends Fragment {
    private HarcamaViewModel viewModel;
    private RecyclerView recyclerViewHarcama;
    private HarcamaAdapter adapter;
    private TextView textToplamHarcama, textKategoriDetay;
    private User user;
    private String currentUserName;
    private Spinner spinnerCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_harcama, container, false);

        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(HarcamaViewModel.class);

        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        String[] categories = {"Tümü", "Market", "Ulaşım", "Fatura", "Eğlence", "Sağlık", "Diğer"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, categories);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterSpinner);

        recyclerViewHarcama = view.findViewById(R.id.recyclerViewHarcama);
        recyclerViewHarcama.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HarcamaAdapter();
        recyclerViewHarcama.setAdapter(adapter);

        textToplamHarcama = view.findViewById(R.id.text_total_harcama);
        textKategoriDetay = view.findViewById(R.id.text_kategori_detay);

        user = SessionManager.getInstance().getUser();
        currentUserName = (user != null) ? user.getUserName() : "default_user";

        viewModel.getHarcamalarLiveData().observe(getViewLifecycleOwner(), harcamaList -> {
            String selectedCategory = spinnerCategory.getSelectedItem().toString();
            if (selectedCategory.equals("Tümü")) {
                adapter.setHarcamalar(harcamaList);
            } else {
                adapter.setHarcamalarFiltered(harcamaList, selectedCategory);
            }
        });

        viewModel.getToplamHarcama().observe(getViewLifecycleOwner(), toplam -> {
            textToplamHarcama.setText(String.format("Toplam Harcama: %.2f ₺", toplam));
        });

        viewModel.getKategoriBazliHarcama().observe(getViewLifecycleOwner(), kategoriMap -> {
            StringBuilder sb = new StringBuilder("Kategori Harcamaları:\n");
            for (String kategori : kategoriMap.keySet()) {
                sb.append(String.format("- %s: %.2f ₺\n", kategori, kategoriMap.get(kategori)));
            }
            textKategoriDetay.setText(sb.toString());
        });

        adapter.setOnHarcamaClickListener(harcama -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Silme Onayı")
                    .setMessage("Bu harcamayı silmek istediğinize emin misiniz?")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        viewModel.deleteHarcama(currentUserName, harcama.getNote());
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.getHarcamalarAsync(currentUserName); // Liste ve toplam otomatik güncellenecek
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        viewModel.getHarcamalarAsync(currentUserName);

        Button addButton = view.findViewById(R.id.btnAddHarcama);
        EditText edtAmount = view.findViewById(R.id.edtAmount);
        EditText edtNote = view.findViewById(R.id.edtNote);

        addButton.setOnClickListener(v -> {
            try {
                double amount = Double.parseDouble(edtAmount.getText().toString());
                String note = edtNote.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();

                viewModel.addHarcama(currentUserName, amount, category, note);

                edtAmount.setText("");
                edtNote.setText("");
                spinnerCategory.setSelection(0);

            } catch (NumberFormatException e) {
                Log.e("HarcamaFragment", "Geçersiz sayı formatı", e);
            }
        });

        return view;
    }
}
