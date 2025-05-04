package com.example.saklasamani.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.saklasamani.R;
import com.example.saklasamani.manager.YatirimManager;
import com.example.saklasamani.model.*;

public class YatirimFragment extends Fragment {

    private Spinner spinnerYatirimTuru;
    private EditText editTextIsim, editTextAdet, editTextBirimFiyat;
    private Button buttonHesapla;
    private TextView textViewSonuc;

    private YatirimManager yatirimManager; // 🔹 Merkez yatırım yöneticisi

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_yatirim, container, false);

        // YatirimManager başlat
        yatirimManager = new YatirimManager();

        // View'ları bağla
        spinnerYatirimTuru = root.findViewById(R.id.spinnerYatirimTuru);
        editTextIsim = root.findViewById(R.id.editTextYatirimIsmi); // ✅ doğru id
        editTextAdet = root.findViewById(R.id.editTextAdet);
        editTextBirimFiyat = root.findViewById(R.id.editTextBirimFiyat);
        buttonHesapla = root.findViewById(R.id.buttonHesapla);
        textViewSonuc = root.findViewById(R.id.textViewSonuc);

        // Spinner için adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.yatirim_turleri,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYatirimTuru.setAdapter(adapter);

        // Buton dinleyicisi
        buttonHesapla.setOnClickListener(v -> {
            try {
                String isim = editTextIsim.getText().toString();
                double adet = Double.parseDouble(editTextAdet.getText().toString());
                double birimFiyat = Double.parseDouble(editTextBirimFiyat.getText().toString());
                String tur = spinnerYatirimTuru.getSelectedItem().toString();

                Yatirim yatirim;

                switch (tur) {
                    case "Coin":
                        //yatirim = new Coin(isim, adet, birimFiyat);
                        break;
                    case "Hisse":
                        //yatirim = new Borsa(isim, adet, birimFiyat);
                        break;
                    case "Döviz":
                        //yatirim = new DegerliMaden(isim, adet, birimFiyat); // geçici olarak
                        break;
                    default:
                        throw new IllegalArgumentException("Geçersiz yatırım türü!");
                }

                // Yatırımı listeye ekle
                //yatirimManager.yatirimEkle(yatirim);

                //double toplam = yatirim.yatirimTutariHesapla();
                //textViewSonuc.setText(tur + " yatırımınız (" + isim + ") toplam: " + toplam + "₺");

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Lütfen geçerli sayılar girin.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
