package com.example.saklasamani.ui.harcama;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.saklasamani.R;

public class HarcamaFragment extends Fragment {

    private HarcamaViewModel viewModel;
    private String currentUserName;
    private RecyclerView recyclerViewHarcama;
    private HarcamaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_harcama, container, false);

        recyclerViewHarcama = view.findViewById(R.id.recyclerViewHarcama);
        recyclerViewHarcama.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HarcamaAdapter();
        recyclerViewHarcama.setAdapter(adapter);

        // ARGÜMANLARI GÜVENLİ ŞEKİLDE AL
        if (getArguments() != null && getArguments().containsKey("userName")) {
            currentUserName = getArguments().getString("userName");
        } else {
            currentUserName = "default_user"; // hata olmasın diye varsayılan değer
            Log.e("HarcamaFragment", "getArguments() null veya 'userName' key'i eksik.");
        }

        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(HarcamaViewModel.class);

// Sadece LiveData'yı observe et
        viewModel.getHarcamalarLiveData().observe(getViewLifecycleOwner(), harcamaList -> {
            adapter.setHarcamalar(harcamaList);
        });

// Veriyi getir (bu arka planda çalışır)
        viewModel.getHarcamalarAsync(currentUserName);

        // GİRİŞ ALANI VE BUTONLAR
        Button addButton = view.findViewById(R.id.btnAddHarcama);
        EditText edtAmount = view.findViewById(R.id.edtAmount);
        EditText edtNote = view.findViewById(R.id.edtNote);

        addButton.setOnClickListener(v -> {
            try {
                double amount = Double.parseDouble(edtAmount.getText().toString());
                String note = edtNote.getText().toString();
                viewModel.addHarcama(currentUserName, amount, note);

                // Temizleme
                edtAmount.setText("");
                edtNote.setText("");

            } catch (NumberFormatException e) {
                Log.e("HarcamaFragment", "Geçersiz sayı formatı", e);
            }
        });

        return view;
    }
}
