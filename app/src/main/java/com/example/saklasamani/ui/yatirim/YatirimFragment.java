package com.example.saklasamani.ui.yatirim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saklasamani.R;
import com.example.saklasamani.manager.YatirimManager;
import com.example.saklasamani.model.yatirim.Borsa;
import com.example.saklasamani.model.yatirim.Coin;
import com.example.saklasamani.model.yatirim.DegerliMaden;
import com.example.saklasamani.model.yatirim.Doviz;
import com.example.saklasamani.model.yatirim.Yatirim;
import java.util.List;
import java.util.Locale;

public class YatirimFragment extends Fragment {

    private Button buttonYeniYatirimEkle, btnHesapla;
    private Spinner spinnerYatirimTuru, spinnerDovizCinsi, spinnerMadenTuru;
    private EditText etYatirimIsmi, etAdet, etBirimFiyat;
    private EditText etCoinSembol, etCoinTipi, etSirketAdi, etHisseSembol;
    private LinearLayout layoutCoin, layoutHisse, layoutDoviz, layoutMaden;
    private TextView textViewToplamTutar, textViewYatirimTuruLabel;
    private RecyclerView recyclerViewYatirimlar;
    private YatirimAdapter yatirimAdapter;
    private YatirimManager yatirimManager;
    private boolean formGorunur = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_yatirim, container, false);

        // Bağlantılar
        buttonYeniYatirimEkle = root.findViewById(R.id.buttonYeniYatirimEkle);
        btnHesapla = root.findViewById(R.id.buttonHesapla);
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
        recyclerViewYatirimlar = root.findViewById(R.id.recyclerViewYatirimlar);
        recyclerViewYatirimlar.setLayoutManager(new LinearLayoutManager(getContext()));
        textViewToplamTutar = root.findViewById(R.id.textViewToplamTutar);
        textViewYatirimTuruLabel = root.findViewById(R.id.textViewYatirimTuruLabel);

        yatirimManager = new YatirimManager();
        yatirimAdapter = new YatirimAdapter(yatirimManager.getYatirimListesi());
        recyclerViewYatirimlar.setAdapter(yatirimAdapter);

        // Spinner'lara veri yükle
        initSpinners();

        // Yeni Yatırım Ekle Butonu Dinleyicisi
        buttonYeniYatirimEkle.setOnClickListener(v -> {
            formGorunur = !formGorunur;
            guncelleFormGorunurlugu();
        });

        // Yatırım türü seçimi
        spinnerYatirimTuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private int lastSelected = -1;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != lastSelected) {
                    String secilenTur = parent.getItemAtPosition(position).toString();
                    guncelleGirisAlanlari(secilenTur);
                    lastSelected = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



        btnHesapla.setOnClickListener(v -> kaydetYatirim());

        // Başlangıçta toplam tutarı göster
        guncelleToplamTutar();

        return root;
    }

    private void guncelleFormGorunurlugu() {
        int gorunurluk = formGorunur ? View.VISIBLE : View.GONE;
        textViewYatirimTuruLabel.setVisibility(gorunurluk);
        spinnerYatirimTuru.setVisibility(gorunurluk);

        // Diğer tüm form elemanlarını GİZLE — Yalnızca tür seçilince gösterilecek
        etYatirimIsmi.setVisibility(View.GONE);
        etAdet.setVisibility(View.GONE);
        etBirimFiyat.setVisibility(View.GONE);
        layoutCoin.setVisibility(View.GONE);
        layoutHisse.setVisibility(View.GONE);
        layoutDoviz.setVisibility(View.GONE);
        layoutMaden.setVisibility(View.GONE);
        btnHesapla.setVisibility(View.GONE);

        if (formGorunur) {
            temizleAlanlar(); // Form görünür olduğunda alanları temizle
        }
    }



    private void guncelleGirisAlanlari(String tur) {
        // Tüm özel layout'ları başlangıçta gizle
        layoutCoin.setVisibility(View.GONE);
        layoutHisse.setVisibility(View.GONE);
        layoutDoviz.setVisibility(View.GONE);
        layoutMaden.setVisibility(View.GONE);

        // Ortak alanları görünür yap
        etYatirimIsmi.setVisibility(View.VISIBLE);
        etAdet.setVisibility(View.VISIBLE);
        etBirimFiyat.setVisibility(View.VISIBLE);
        btnHesapla.setVisibility(View.VISIBLE);

        // Seçilen türe göre ilgili layout'u ve spinner'ları görünür yap
        if (tur.equals("Coin")) {
            layoutCoin.setVisibility(View.VISIBLE);
        } else if (tur.equals("Hisse")) {
            layoutHisse.setVisibility(View.VISIBLE);
        } else if (tur.equals("Döviz")) {
            layoutDoviz.setVisibility(View.VISIBLE);
        } else if (tur.equals("Maden")) {
            layoutMaden.setVisibility(View.VISIBLE);
        }
    }

    private void guncelleYatirimListesi() {
        List<Yatirim> guncelListe = yatirimManager.getYatirimListesi();
        yatirimAdapter.setYatirimListesi(guncelListe);
        guncelleToplamTutar(); // Liste güncellendiğinde toplam tutarı da güncelle
    }

    private void guncelleToplamTutar() {
        double toplam = yatirimManager.toplamYatirimTutari();
        textViewToplamTutar.setText(String.format(Locale.getDefault(), "Toplam Tutar: %.2f ₺", toplam));
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> adapterTur = ArrayAdapter.createFromResource(getContext(), R.array.yatirim_turleri, android.R.layout.simple_spinner_item);
        spinnerYatirimTuru.setAdapter(adapterTur);

        ArrayAdapter<CharSequence> adapterDoviz = ArrayAdapter.createFromResource(getContext(), R.array.doviz_turleri, android.R.layout.simple_spinner_item);
        spinnerDovizCinsi.setAdapter(adapterDoviz);

        ArrayAdapter<CharSequence> adapterMaden = ArrayAdapter.createFromResource(getContext(), R.array.maden_turleri, android.R.layout.simple_spinner_item);
        spinnerMadenTuru.setAdapter(adapterMaden);
    }

    private void kaydetYatirim() {
        String isim = etYatirimIsmi.getText().toString();
        String tur = spinnerYatirimTuru.getSelectedItem().toString();

        if (isim.isEmpty() || etAdet.getText().toString().isEmpty() || etBirimFiyat.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Lütfen gerekli alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double adet = Double.parseDouble(etAdet.getText().toString());
            double fiyat = Double.parseDouble(etBirimFiyat.getText().toString());

            Yatirim yatirim = null;

            switch (tur) {
                case "Coin":
                    yatirim = new Coin(isim, adet, fiyat,
                            etCoinSembol.getText().toString(),
                            etCoinTipi.getText().toString());
                    break;
                case "Hisse":
                    yatirim = new Borsa(isim, adet, fiyat,
                            etSirketAdi.getText().toString(),
                            etHisseSembol.getText().toString());
                    break;
                case "Döviz":
                    String dovizCinsi = spinnerDovizCinsi.getSelectedItem().toString();
                    yatirim = new Doviz(isim, adet, fiyat, dovizCinsi);
                    break;
                case "Maden":
                    String madenTuru = spinnerMadenTuru.getSelectedItem().toString();
                    yatirim = new DegerliMaden(isim, adet, fiyat, madenTuru);
                    break;
                default:
                    Toast.makeText(getContext(), "Geçersiz yatırım türü!", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (yatirim != null) {
                yatirimManager.yatirimEkle(yatirim);
                guncelleYatirimListesi(); // Yatırım eklendikten sonra listeyi ve toplamı güncelle
                temizleAlanlar();
                formGorunur = false; // Formu kapat
                guncelleFormGorunurlugu();
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

        // yatırım türünü sıfırla (ilk seçeneğe getir)
        spinnerYatirimTuru.setSelection(0);
    }

}