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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.saklasamani.R;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.User;

public class HarcamaFragment extends Fragment {
    private HarcamaViewModel viewModel;
    private RecyclerView recyclerViewHarcama;
    private HarcamaAdapter adapter;
    private TextView textToplamHarcama;
    private User user;
    private String currentUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_harcama, container, false);

        recyclerViewHarcama = view.findViewById(R.id.recyclerViewHarcama);
        recyclerViewHarcama.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HarcamaAdapter();
        recyclerViewHarcama.setAdapter(adapter);

        textToplamHarcama = view.findViewById(R.id.text_total_harcama);

        // Kullanıcıyı session'dan al
        user = SessionManager.getInstance().getUser();

        if (user != null) {
            currentUserName = user.getUserName();
        } else {
            currentUserName = "default_user";
            Log.e("HarcamaFragment", "SessionManager'dan kullanıcı alınamadı.");
        }

        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(HarcamaViewModel.class);

        viewModel.getHarcamalarLiveData().observe(getViewLifecycleOwner(), harcamaList -> {
            adapter.setHarcamalar(harcamaList);
        });

        viewModel.getToplamHarcama().observe(getViewLifecycleOwner(), toplam -> {
            textToplamHarcama.setText(String.format("Toplam Harcama: %.2f ₺", toplam));
        });

        viewModel.getHarcamalarAsync(currentUserName);

        Button addButton = view.findViewById(R.id.btnAddHarcama);
        EditText edtAmount = view.findViewById(R.id.edtAmount);
        EditText edtNote = view.findViewById(R.id.edtNote);

        addButton.setOnClickListener(v -> {
            try {
                double amount = Double.parseDouble(edtAmount.getText().toString());
                String note = edtNote.getText().toString();
                viewModel.addHarcama(currentUserName, amount, note);

                edtAmount.setText("");
                edtNote.setText("");

            } catch (NumberFormatException e) {
                Log.e("HarcamaFragment", "Geçersiz sayı formatı", e);
            }
        });

        return view;
    }
}
