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
import java.util.Arrays;
import java.util.List;

public class YatirimFragment extends Fragment {

    private Spinner spinnerYatirimTuru;
    private EditText editTextYatirimIsmi, editTextAdet, editTextBirimFiyat;
    private EditText editTextCoinSembol, editTextCoinTipi;
    private EditText editTextSirketAdi, editTextSembol;
    private Spinner spinnerDovizCinsi;
    private Spinner spinnerMadenTuru;
    private Button buttonHesapla;
    private TextView textViewSonuc;

    private YatirimManager yatirimManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_yatirim, container, false);

        yatirimManager = new YatirimManager();

        // UI elemanlarını bağla
        spinnerYatirimTuru = root.findViewById(R.id.spinnerYatirimTuru);
        editTextYatirimIsmi = root.findViewById(R.id.editTextYatirimIsmi);
        editTextAdet = root.findViewById(R.id.editTextAdet);
        editTextBirimFiyat = root.findViewById(R.id.editTextBirimFiyat);
        editTextCoinSembol = root.findViewById(R.id.editTextCoinSembol);
        editTextCoinTipi = root.findViewById(R.id.editTextCoinTipi);
        editTextSirketAdi = root.findViewById(R.id.editTextSirketAdi);
        editTextSembol = root.findViewById(R.id.editTextSembol);
        spinnerDovizCinsi = root.findViewById(R.id.spinnerDovizCinsi);
        spinnerMadenTuru = root.findViewById(R.id.spinnerMadenTuru);
        buttonHesapla = root.findViewById(R.id.buttonHesapla);
        textViewSonuc = root.findViewById(R.id.textViewSonuc);

        // Spinner için adapter
        ArrayAdapter<CharSequence> adapterYatirimTuru = ArrayAdapter.createFromResource(
                getContext(),
                R.array.yatirim_turleri,
                android.R.layout.simple_spinner_item
        );
        adapterYatirimTuru.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYatirimTuru.setAdapter(adapterYatirimTuru);

        // Döviz cinsi spinner için adapter
        List<String> dovizListesi = Arrays.asList("USD", "EUR", "GBP", "JPY", "CAD"); // Örnek dövizler
        ArrayAdapter<String> adapterDovizCinsi = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                dovizListesi
        );
        adapterDovizCinsi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDovizCinsi.setAdapter(adapterDovizCinsi);

        // Maden türü spinner için adapter (strings.xml'den alınıyor)
        ArrayAdapter<CharSequence> adapterMadenTuru = ArrayAdapter.createFromResource(
                getContext(),
                R.array.maden_turleri,
                android.R.layout.simple_spinner_item
        );
        adapterMadenTuru.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMadenTuru.setAdapter(adapterMadenTuru);

        // Spinner seçim dinleyicisi
        spinnerYatirimTuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String secilenTur = parent.getItemAtPosition(position).toString();
                guncelleGirisAlanlari(secilenTur);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Bir şey seçilmezse yapılacak işlemler (isteğe bağlı)
            }
        });

        // Hesapla butonu dinleyicisi
        buttonHesapla.setOnClickListener(v -> kaydetYatirim());

        // Başlangıçta giriş alanlarını güncelle
        guncelleGirisAlanlari(spinnerYatirimTuru.getSelectedItem().toString());

        return root;
    }

    // Yatırım türüne göre giriş alanlarını göster/gizle
    private void guncelleGirisAlanlari(String tur) {
        editTextCoinSembol.setVisibility(tur.equals("Coin") ? View.VISIBLE : View.GONE);
        editTextCoinTipi.setVisibility(tur.equals("Coin") ? View.VISIBLE : View.GONE);
        editTextSirketAdi.setVisibility(tur.equals("Hisse") ? View.VISIBLE : View.GONE);
        editTextSembol.setVisibility(tur.equals("Hisse") ? View.VISIBLE : View.GONE);
        spinnerDovizCinsi.setVisibility(tur.equals("Döviz") ? View.VISIBLE : View.GONE);
        spinnerMadenTuru.setVisibility(tur.equals("Altın") || tur.equals("Gümüş") ? View.VISIBLE : View.GONE);
    }

    // Yatırımı kaydet ve sonucu göster
    private void kaydetYatirim() {
        try {
            String isim = editTextYatirimIsmi.getText().toString();
            double adet = Double.parseDouble(editTextAdet.getText().toString());
            double birimFiyat = Double.parseDouble(editTextBirimFiyat.getText().toString());
            String tur = spinnerYatirimTuru.getSelectedItem().toString();

            Yatirim yatirim = null;

            switch (tur) {
                case "Coin":
                    String sembol = editTextCoinSembol.getText().toString();
                    String tip = editTextCoinTipi.getText().toString();
                    yatirim = new Coin(isim, adet, birimFiyat, sembol, tip);
                    break;
                case "Hisse":
                    String sirket = editTextSirketAdi.getText().toString();
                    String sembolHisse = editTextSembol.getText().toString();
                    yatirim = new Borsa(isim, adet, birimFiyat, sirket, sembolHisse);
                    break;
                case "Döviz":
                    String dovizCinsi = spinnerDovizCinsi.getSelectedItem().toString();
                    yatirim = new Doviz(isim, adet, birimFiyat, dovizCinsi);
                    break;
                case "Altın":
                case "Gümüş":
                    String madenTuru = spinnerMadenTuru.getSelectedItem().toString();
                    yatirim = new DegerliMaden(isim, adet, birimFiyat, madenTuru);
                    break;
                default:
                    throw new IllegalArgumentException("Geçersiz yatırım türü!");
            }

            if (yatirim != null) {
                yatirimManager.yatirimEkle(yatirim);
                double toplam = yatirim.yatirimTutariHesapla();
                textViewSonuc.setText(tur + " yatırımınız (" + isim + ") toplam: " + String.format("%.2f", toplam) + "₺");
                // Giriş alanlarını temizleyebiliriz (isteğe bağlı)
                temizleGirisAlanlari();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Lütfen geçerli sayılar girin.", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Giriş alanlarını temizle (isteğe bağlı)
    private void temizleGirisAlanlari() {
        editTextYatirimIsmi.getText().clear();
        editTextAdet.getText().clear();
        editTextBirimFiyat.getText().clear();
        editTextCoinSembol.getText().clear();
        editTextCoinTipi.getText().clear();
        editTextSirketAdi.getText().clear();
        editTextSembol.getText().clear();
        // Spinner'ları ilk seçeneğe döndürebiliriz (isteğe bağlı)
    }
}