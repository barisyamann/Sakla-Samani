package com.example.saklasamani.ui.yatirim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.saklasamani.R;
import com.example.saklasamani.manager.YatirimManager;
import com.example.saklasamani.model.yatirim.Borsa;
import com.example.saklasamani.model.yatirim.Coin;
import com.example.saklasamani.model.yatirim.DegerliMaden;
import com.example.saklasamani.model.yatirim.Doviz;
import com.example.saklasamani.model.yatirim.Yatirim;

public class YatirimFragment extends Fragment {

    private Spinner spinnerYatirimTuru, spinnerDovizCinsi, spinnerMadenTuru;
    private EditText etYatirimIsmi, etAdet, etBirimFiyat;
    private EditText etCoinSembol, etCoinTipi, etSirketAdi, etHisseSembol;
    private TextView tvSonuc;
    private Button btnHesapla;

    private LinearLayout layoutCoin, layoutHisse, layoutDoviz, layoutMaden;

    private YatirimManager yatirimManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_yatirim, container, false);

        // Bağlantılar
        spinnerYatirimTuru = root.findViewById(R.id.spinnerYatirimTuru);
        spinnerDovizCinsi = root.findViewById(R.id.spinnerDovizCinsi);
        spinnerMadenTuru = root.findViewById(R.id.spinnerMadenTuru);

        etYatirimIsmi = root.findViewById(R.id.editTextYatirimIsmi);
        etAdet = root.findViewById(R.id.editTextAdet);
        etBirimFiyat = root.findViewById(R.id.editTextBirimFiyat);

        etCoinSembol = root.findViewById(R.id.editTextCoinSembol);
        etCoinTipi = root.findViewById(R.id.editTextCoinTipi);
        etSirketAdi = root.findViewById(R.id.editTextSirketAdi);
        etHisseSembol = root.findViewById(R.id.editTextSembol);

        layoutCoin = root.findViewById(R.id.layoutCoin);
        layoutHisse = root.findViewById(R.id.layoutHisse);
        layoutDoviz = root.findViewById(R.id.layoutDoviz);
        layoutMaden = root.findViewById(R.id.layoutMaden);

        tvSonuc = root.findViewById(R.id.textViewSonuc);
        btnHesapla = root.findViewById(R.id.buttonHesapla);

        yatirimManager = new YatirimManager();

        // Spinner'lara veri yükle
        initSpinners();

        // Yatırım türü seçimi
        spinnerYatirimTuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String secilenTur = parent.getItemAtPosition(position).toString();
                guncelleGirisAlanlari(secilenTur);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnHesapla.setOnClickListener(v -> kaydetYatirim());

        return root;
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> adapterTur = ArrayAdapter.createFromResource(getContext(), R.array.yatirim_turleri, android.R.layout.simple_spinner_item);
        spinnerYatirimTuru.setAdapter(adapterTur);

        ArrayAdapter<CharSequence> adapterDoviz = ArrayAdapter.createFromResource(getContext(), R.array.doviz_turleri, android.R.layout.simple_spinner_item);
        spinnerDovizCinsi.setAdapter(adapterDoviz);

        ArrayAdapter<CharSequence> adapterMaden = ArrayAdapter.createFromResource(getContext(), R.array.maden_turleri, android.R.layout.simple_spinner_item);
        spinnerMadenTuru.setAdapter(adapterMaden);
    }

    private void guncelleGirisAlanlari(String tur) {
        layoutCoin.setVisibility(tur.equals("Coin") ? View.VISIBLE : View.GONE);
        layoutHisse.setVisibility(tur.equals("Hisse") ? View.VISIBLE : View.GONE);
        layoutDoviz.setVisibility(tur.equals("Döviz") ? View.VISIBLE : View.GONE);
        layoutMaden.setVisibility((tur.equals("Altın") || tur.equals("Gümüş")) ? View.VISIBLE : View.GONE);
    }

    private void kaydetYatirim() {
        try {
            String isim = etYatirimIsmi.getText().toString();
            double adet = Double.parseDouble(etAdet.getText().toString());
            double fiyat = Double.parseDouble(etBirimFiyat.getText().toString());
            String tur = spinnerYatirimTuru.getSelectedItem().toString();

            Yatirim yatirim = null;

            switch (tur) {
                case "Coin":
                    yatirim = new Coin(isim, adet, fiyat, etCoinSembol.getText().toString(), etCoinTipi.getText().toString());
                    break;
                case "Hisse":
                    yatirim = new Borsa(isim, adet, fiyat, etSirketAdi.getText().toString(), etHisseSembol.getText().toString());
                    break;
                case "Döviz":
                    yatirim = new Doviz(isim, adet, fiyat, spinnerDovizCinsi.getSelectedItem().toString());
                    break;
                case "Altın":
                case "Gümüş":
                    yatirim = new DegerliMaden(isim, adet, fiyat, spinnerMadenTuru.getSelectedItem().toString());
                    break;
            }

            if (yatirim != null) {
                yatirimManager.yatirimEkle(yatirim);
                double toplam = yatirim.yatirimTutariHesapla();
                tvSonuc.setText(tur + " yatırımınız (" + isim + ") toplam: " + String.format("%.2f", toplam) + "₺");
                temizleAlanlar();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Lütfen geçerli sayı girin", Toast.LENGTH_SHORT).show();
        }
    }

    private void temizleAlanlar() {
        etYatirimIsmi.setText("");
        etAdet.setText("");
        etBirimFiyat.setText("");
        etCoinSembol.setText("");
        etCoinTipi.setText("");
        etSirketAdi.setText("");
        etHisseSembol.setText("");
    }
}
