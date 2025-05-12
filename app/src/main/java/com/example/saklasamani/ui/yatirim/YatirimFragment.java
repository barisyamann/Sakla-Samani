package com.example.saklasamani.ui.yatirim;

import android.app.AlertDialog;
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

public class YatirimFragment extends Fragment implements YatirimAdapter.OnYatirimClickListener {

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
    private int duzenlenenYatirimPozisyonu = -1; // Düzenlenen yatırımın pozisyonunu tutar

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

        yatirimManager = YatirimManager.getInstance(); // Singleton örneğini al
        yatirimAdapter = new YatirimAdapter(yatirimManager.getYatirimListesi(), this); // 'this' ile listener'ı fragment'a bağla
        recyclerViewYatirimlar.setAdapter(yatirimAdapter);

        // Spinner'lara veri yükle
        initSpinners();

        // Yeni Yatırım Ekle Butonu Dinleyicisi
        buttonYeniYatirimEkle.setOnClickListener(v -> {
            formGorunur = true; // Formu görünür yap
            duzenlenenYatirimPozisyonu = -1; // Yeni ekleme moduna geçildiğinde düzenleme pozisyonunu sıfırla
            guncelleFormGorunurlugu();
            btnHesapla.setText(R.string.yatirimi_kaydet); // Buton metnini güncelle
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

        btnHesapla.setOnClickListener(v -> {
            if (duzenlenenYatirimPozisyonu != -1) {
                duzenleYatirim();
            } else {
                kaydetYatirim();
            }
        });

        // Başlangıçta toplam tutarı göster
        guncelleToplamTutar();

        return root;
    }

    private void guncelleFormGorunurlugu() {
        int gorunurluk = formGorunur ? View.VISIBLE : View.GONE;

        textViewYatirimTuruLabel.setVisibility(gorunurluk);
        spinnerYatirimTuru.setVisibility(gorunurluk);
        etYatirimIsmi.setVisibility(gorunurluk);
        etAdet.setVisibility(gorunurluk);
        etBirimFiyat.setVisibility(gorunurluk);
        layoutCoin.setVisibility(gorunurluk);
        layoutHisse.setVisibility(gorunurluk);
        layoutDoviz.setVisibility(gorunurluk);
        layoutMaden.setVisibility(gorunurluk);
        btnHesapla.setVisibility(gorunurluk);

        if (formGorunur) {
            temizleAlanlar(); // Form görünür olduğunda alanları temizle
            // Form ilk açıldığında tüm özel alanlar gizli kalsın, tür seçimiyle açılsın
            layoutCoin.setVisibility(View.GONE);
            layoutHisse.setVisibility(View.GONE);
            layoutDoviz.setVisibility(View.GONE);
            layoutMaden.setVisibility(View.GONE);
        } else {
            // Form gizlendiğinde tüm özel alanları da gizle
            layoutCoin.setVisibility(View.GONE);
            layoutHisse.setVisibility(View.GONE);
            layoutDoviz.setVisibility(View.GONE);
            layoutMaden.setVisibility(View.GONE);
        }
    }

    private void guncelleGirisAlanlari(String tur) {
        // Tüm özel layout'ları ve spinner'ları başlangıçta gizle
        layoutCoin.setVisibility(View.GONE);
        layoutHisse.setVisibility(View.GONE);
        layoutDoviz.setVisibility(View.GONE);
        layoutMaden.setVisibility(View.GONE);
        spinnerDovizCinsi.setVisibility(View.GONE);
        spinnerMadenTuru.setVisibility(View.GONE);

        // Ortak alanları görünür yap
        etYatirimIsmi.setVisibility(View.VISIBLE);
        etAdet.setVisibility(View.VISIBLE);
        etBirimFiyat.setVisibility(View.VISIBLE);

        // Seçilen türe göre ilgili layout'u ve spinner'ları görünür yap
        if (tur.equals("Coin")) {
            layoutCoin.setVisibility(View.VISIBLE);
        } else if (tur.equals("Hisse")) {
            layoutHisse.setVisibility(View.VISIBLE);
        } else if (tur.equals("Döviz")) {
            layoutDoviz.setVisibility(View.VISIBLE);
            spinnerDovizCinsi.setVisibility(View.VISIBLE); // Döviz spinner'ını görünür yap
        } else if (tur.equals("Maden")) {
            layoutMaden.setVisibility(View.VISIBLE);
            spinnerMadenTuru.setVisibility(View.VISIBLE); // Maden spinner'ını görünür yap
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
            Toast.makeText(getContext(), R.string.lutfen_gerekli_alanlari_doldurun, Toast.LENGTH_SHORT).show(); // String kaynağı kullanıldı
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
                    Toast.makeText(getContext(), R.string.gecersiz_yatirim_turu, Toast.LENGTH_SHORT).show(); // String kaynağı kullanıldı
                    return;
            }

            if (yatirim != null) {
                yatirimManager.yatirimEkle(yatirim);
                guncelleYatirimListesi(); // Yatırım eklendikten sonra listeyi ve toplamı güncelle
                temizleAlanlar(); // Burada temizleniyor
                formGorunur = false; // Formu kapat
                guncelleFormGorunurlugu();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), R.string.gecerli_sayi_girin, Toast.LENGTH_SHORT).show(); // String kaynağı kullanıldı
        }
    }

    private void duzenleYatirim() {
        if (duzenlenenYatirimPozisyonu != -1) {
            String isim = etYatirimIsmi.getText().toString();
            String tur = spinnerYatirimTuru.getSelectedItem().toString();

            if (isim.isEmpty() || etAdet.getText().toString().isEmpty() || etBirimFiyat.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), R.string.lutfen_gerekli_alanlari_doldurun, Toast.LENGTH_SHORT).show(); // String kaynağı kullanıldı
                return;
            }

            try {
                double adet = Double.parseDouble(etAdet.getText().toString());
                double fiyat = Double.parseDouble(etBirimFiyat.getText().toString());

                Yatirim guncellenenYatirim = null;

                switch (tur) {
                    case "Coin":
                        guncellenenYatirim = new Coin(isim, adet, fiyat,
                                etCoinSembol.getText().toString(),
                                etCoinTipi.getText().toString());
                        break;
                    case "Hisse":
                        guncellenenYatirim = new Borsa(isim, adet, fiyat,
                                etSirketAdi.getText().toString(),
                                etHisseSembol.getText().toString());
                        break;
                    case "Döviz":
                        String dovizCinsi = spinnerDovizCinsi.getSelectedItem().toString();
                        guncellenenYatirim = new Doviz(isim, adet, fiyat, dovizCinsi);
                        break;
                    case "Maden":
                        String madenTuru = spinnerMadenTuru.getSelectedItem().toString();
                        guncellenenYatirim = new DegerliMaden(isim, adet, fiyat, madenTuru);
                        break;
                    default:
                        Toast.makeText(getContext(), R.string.gecersiz_yatirim_turu, Toast.LENGTH_SHORT).show(); // String kaynağı kullanıldı
                        return;
                }

                if (guncellenenYatirim != null) {
                    yatirimManager.getYatirimListesi().set(duzenlenenYatirimPozisyonu, guncellenenYatirim);
                    guncelleYatirimListesi();
                    temizleAlanlar();
                    formGorunur = false;
                    guncelleFormGorunurlugu();
                    duzenlenenYatirimPozisyonu = -1; // Düzenleme tamamlandı, pozisyonu sıfırla
                    btnHesapla.setText(R.string.yatirimi_kaydet); // Buton metnini tekrar ayarla (string kaynağı kullanıldı)
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), R.string.gecerli_sayi_girin, Toast.LENGTH_SHORT).show(); // String kaynağı kullanıldı
            }
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
        spinnerYatirimTuru.setSelection(0);
    }

    @Override
    public void onSilClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.yatirimi_sil); // String kaynağını kullanın: "Yatırımı Sil"
        builder.setMessage(R.string.yatirimi_silmek_istediginize_emin_misiniz); // String kaynağını kullanın: "Bu yatırımı silmek istediğinize emin misiniz?"

        builder.setPositiveButton(R.string.evet, (dialog, which) -> { // String kaynağını kullanın: "Evet"
            // Silme işlemini gerçekleştir
            yatirimManager.yatirimSil(position);
            yatirimAdapter.setYatirimListesi(yatirimManager.getYatirimListesi());
            guncelleToplamTutar();

            // Eğer düzenleme modundaysak ve o yatırım silindiyse formu sıfırla
            if (position == duzenlenenYatirimPozisyonu) {
                duzenlenenYatirimPozisyonu = -1;
                temizleAlanlar();
                formGorunur = false;
                guncelleFormGorunurlugu();
                btnHesapla.setText(R.string.yatirimi_kaydet);
            }
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.hayir, (dialog, which) -> { // String kaynağını kullanın: "Hayır"
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onDuzenleClick(int position) {
        Yatirim yatirim = yatirimManager.getYatirimListesi().get(position);
        duzenlenenYatirimPozisyonu = position;

        etYatirimIsmi.setText(yatirim.getYatirimIsmi());
        etAdet.setText(String.valueOf(yatirim.getYatirimAdeti()));
        etBirimFiyat.setText(String.valueOf(yatirim.getYatirimBirimFiyati()));

        String yatirimTuru = "";
        if (yatirim instanceof Coin) {
            yatirimTuru = "Coin";
            spinnerYatirimTuru.setSelection(0);
            Coin coin = (Coin) yatirim;
            etCoinSembol.setText(coin.getCoinSembol());
            etCoinTipi.setText(coin.getCoinTipi());
        } else if (yatirim instanceof Borsa) {
            yatirimTuru = "Hisse";
            spinnerYatirimTuru.setSelection(1);
            Borsa borsa = (Borsa) yatirim;
            etSirketAdi.setText(borsa.getSirketAdi());
            etHisseSembol.setText(borsa.getSembol());
        } else if (yatirim instanceof Doviz) {
            yatirimTuru = "Döviz";
            int index = getIndex(spinnerYatirimTuru, yatirimTuru);
            if (index != -1) spinnerYatirimTuru.setSelection(index);
            spinnerDovizCinsi.setSelection(getIndex(spinnerDovizCinsi, ((Doviz) yatirim).getDovizCinsi()));
        } else if (yatirim instanceof DegerliMaden) {
            yatirimTuru = "Maden";
            int index = getIndex(spinnerYatirimTuru, yatirimTuru);
            if (index != -1) spinnerYatirimTuru.setSelection(index);
            spinnerMadenTuru.setSelection(getIndex(spinnerMadenTuru, ((DegerliMaden) yatirim).getMadenTuru()));
        }

        formGorunur = true;
        guncelleFormGorunurlugu();
        // Düzenleme modunda doğru alanların görünmesi için bu metodu çağırın
        guncelleGirisAlanlari(yatirimTuru);
        btnHesapla.setText(R.string.yatirimi_guncelle);
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }


}