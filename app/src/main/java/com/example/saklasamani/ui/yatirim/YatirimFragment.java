package com.example.saklasamani.ui.yatirim;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saklasamani.R;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.data.YatirimDao;
import com.example.saklasamani.manager.YatirimManager;
import com.example.saklasamani.model.User;
import com.example.saklasamani.model.yatirim.*;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;

public class YatirimFragment extends Fragment implements YatirimAdapter.OnYatirimClickListener {

    private RecyclerView recyclerViewYatirimlar;
    private YatirimAdapter adapter;
    private List<Yatirim> yatirimListesi = new ArrayList<>();
    private YatirimDao yatirimDao;
    private SessionManager sessionManager;

    private TextView textViewToplamTutar;
    private Button buttonYeniYatirimEkle;
    private LinearLayout layoutForm;
    private Spinner spinnerYatirimTuru;
    private TextView textViewYatirimTuruLabel;
    private EditText editTextYatirimIsmi, editTextAdet, editTextBirimFiyat;
    private Spinner spinnerDovizCinsi, spinnerMadenTuru;
    private EditText editTextCoinSembol, editTextCoinTipi;
    private EditText editTextSirketAdi, editTextSembol;
    private LinearLayout layoutDoviz, layoutMaden, layoutCoin, layoutHisse;
    private Button buttonHesapla;

    private User aktifKullanici;
    private YatirimManager yatirimManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yatirim, container, false);
        yatirimManager = new YatirimManager(requireContext());

        // SessionManager'dan kullanıcıyı alıyoruz
        sessionManager = SessionManager.getInstance();
        aktifKullanici = sessionManager.getUser();

        if (aktifKullanici == null) {
            Toast.makeText(requireContext(), "Kullanıcı oturumu bulunamadı!", Toast.LENGTH_SHORT).show();
            Log.e("YatirimFragment", "SessionManager.getUser() null döndü!");
            return view;
        }

        String userName = aktifKullanici.getUserName(); // Globalde de kullanacağız

        yatirimDao = new YatirimDao(requireContext());

        // RecyclerView ve adapter kurulumu
        recyclerViewYatirimlar = view.findViewById(R.id.recyclerViewYatirimlar);
        recyclerViewYatirimlar.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new YatirimAdapter(yatirimListesi, this);
        recyclerViewYatirimlar.setAdapter(adapter);

        // Diğer view tanımlamaları
        textViewToplamTutar = view.findViewById(R.id.textViewToplamTutar);
        buttonYeniYatirimEkle = view.findViewById(R.id.buttonYeniYatirimEkle);
        layoutForm = view.findViewById(R.id.fragmentYatirimRoot);

        spinnerYatirimTuru = view.findViewById(R.id.spinnerYatirimTuru);
        textViewYatirimTuruLabel = view.findViewById(R.id.textViewYatirimTuruLabel);
        editTextYatirimIsmi = view.findViewById(R.id.editTextYatirimIsmi);
        editTextAdet = view.findViewById(R.id.editTextAdet);
        editTextBirimFiyat = view.findViewById(R.id.editTextBirimFiyat);
        spinnerDovizCinsi = view.findViewById(R.id.spinnerDovizCinsi);
        spinnerMadenTuru = view.findViewById(R.id.spinnerMadenTuru);
        editTextCoinSembol = view.findViewById(R.id.editTextCoinSembol);
        editTextCoinTipi = view.findViewById(R.id.editTextCoinTipi);
        editTextSirketAdi = view.findViewById(R.id.editTextSirketAdi);
        editTextSembol = view.findViewById(R.id.editTextSembol);

        layoutDoviz = view.findViewById(R.id.layoutDoviz);
        layoutMaden = view.findViewById(R.id.layoutMaden);
        layoutCoin = view.findViewById(R.id.layoutCoin);
        layoutHisse = view.findViewById(R.id.layoutHisse);
        buttonHesapla = view.findViewById(R.id.buttonHesapla);

        // Yatırım türü spinner'ı için adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.yatirim_turleri));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYatirimTuru.setAdapter(spinnerAdapter);

        // Maden türü spinner'ı için adapter
        ArrayAdapter<CharSequence> madenAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.maden_turleri, android.R.layout.simple_spinner_item);
        madenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMadenTuru.setAdapter(madenAdapter);

        // Döviz cinsi spinner'ı için adapter
        ArrayAdapter<CharSequence> dovizAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.doviz_turleri, android.R.layout.simple_spinner_item);
        dovizAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDovizCinsi.setAdapter(dovizAdapter);

        spinnerYatirimTuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gizleAlanlar();
                switch (position) {
                    case 0: layoutCoin.setVisibility(View.VISIBLE); break;
                    case 1: layoutHisse.setVisibility(View.VISIBLE); break;
                    case 2: layoutDoviz.setVisibility(View.VISIBLE); break;
                    case 3: layoutMaden.setVisibility(View.VISIBLE); break;
                }
                editTextYatirimIsmi.setVisibility(View.VISIBLE);
                editTextAdet.setVisibility(View.VISIBLE);
                editTextBirimFiyat.setVisibility(View.VISIBLE);
                buttonHesapla.setVisibility(View.VISIBLE);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonYeniYatirimEkle.setOnClickListener(v -> {
            // Form elemanlarını görünür yap
            spinnerYatirimTuru.setVisibility(View.VISIBLE);
            textViewYatirimTuruLabel.setVisibility(View.VISIBLE);
            editTextYatirimIsmi.setVisibility(View.VISIBLE);
            editTextAdet.setVisibility(View.VISIBLE);
            editTextBirimFiyat.setVisibility(View.VISIBLE);
            buttonHesapla.setVisibility(View.VISIBLE);

            // Form alanlarını temizle
            spinnerYatirimTuru.setSelection(0); // İlk elemanı seçerek varsayılanı getir
            editTextYatirimIsmi.setText("");
            editTextAdet.setText("");
            editTextBirimFiyat.setText("");
            editTextCoinSembol.setText("");
            editTextCoinTipi.setText("");
            editTextSirketAdi.setText("");
            editTextSembol.setText("");
            spinnerDovizCinsi.setSelection(0);
            spinnerMadenTuru.setSelection(0);

            // İlgili alt layoutları görünür yap (varsayılan olarak ilk yatırım türüne göre)
            gizleAlanlar();
            if (spinnerYatirimTuru.getSelectedItemPosition() == 0) {
                layoutCoin.setVisibility(View.VISIBLE);
            } else if (spinnerYatirimTuru.getSelectedItemPosition() == 1) {
                layoutHisse.setVisibility(View.VISIBLE);
            } else if (spinnerYatirimTuru.getSelectedItemPosition() == 2) {
                layoutDoviz.setVisibility(View.VISIBLE);
            } else if (spinnerYatirimTuru.getSelectedItemPosition() == 3) {
                layoutMaden.setVisibility(View.VISIBLE);
            }
        });

        buttonHesapla.setOnClickListener(v -> {
            String isim = editTextYatirimIsmi.getText().toString();
            String adetStr = editTextAdet.getText().toString();
            String birimFiyatStr = editTextBirimFiyat.getText().toString();

            if (isim.isEmpty() || adetStr.isEmpty() || birimFiyatStr.isEmpty()) {
                Toast.makeText(requireContext(), R.string.lutfen_gerekli_alanlari_doldurun, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double adet = Double.parseDouble(adetStr);
                double birimFiyat = Double.parseDouble(birimFiyatStr);

                Yatirim yeniYatirim = null;
                boolean basarili = false;

                switch (spinnerYatirimTuru.getSelectedItemPosition()) {
                    case 0: // Coin
                        String coinSembol = editTextCoinSembol.getText().toString();
                        String coinTipi = editTextCoinTipi.getText().toString();
                        if (coinSembol.isEmpty() || coinTipi.isEmpty()) {
                            Toast.makeText(requireContext(), R.string.lutfen_gerekli_alanlari_doldurun, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Coin coin = new Coin(userName, isim, adet, birimFiyat, coinSembol, coinTipi);
                        basarili = yatirimManager.addCoinInvestment(coin);
                        yeniYatirim = coin;
                        break;
                    case 1: // Borsa
                        String sirketAdi = editTextSirketAdi.getText().toString();
                        String sembol = editTextSembol.getText().toString();
                        if (sirketAdi.isEmpty() || sembol.isEmpty()) {
                            Toast.makeText(requireContext(), R.string.lutfen_gerekli_alanlari_doldurun, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Borsa borsa = new Borsa(userName, isim, adet, birimFiyat, sirketAdi, sembol);
                        basarili = yatirimManager.addBorsaInvestment(borsa);
                        yeniYatirim = borsa;
                        break;
                    case 2: // Döviz
                        String dovizCinsi = spinnerDovizCinsi.getSelectedItem().toString();
                        Doviz doviz = new Doviz(userName, isim, adet, birimFiyat, dovizCinsi);
                        basarili = yatirimManager.addDovizInvestment(doviz);
                        yeniYatirim = doviz;
                        break;
                    case 3: // Değerli Maden
                        String madenTuru = spinnerMadenTuru.getSelectedItem().toString();
                        DegerliMaden maden = new DegerliMaden(userName, isim, adet, birimFiyat, madenTuru);
                        basarili = yatirimManager.addDegerliMadenInvestment(maden);
                        yeniYatirim = maden;
                        break;
                    default:
                        Toast.makeText(requireContext(), R.string.gecersiz_yatirim_turu, Toast.LENGTH_SHORT).show();
                        return;
                }

                if (basarili && yeniYatirim != null) {
                    yatirimListesi.add(yeniYatirim);
                    adapter.notifyItemInserted(yatirimListesi.size() - 1);
                    guncelleToplamTutar();
                    Toast.makeText(requireContext(), "Yatırım eklendi ve bütçeden düşüldü!", Toast.LENGTH_SHORT).show();

                    // Yatırım eklendikten sonra formu temizle ve gizle
                    gizleForm();
                } else {Toast.makeText(requireContext(), "Yetersiz bütçe! Yatırım eklenemedi.", Toast.LENGTH_LONG).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), R.string.gecerli_sayi_girin, Toast.LENGTH_SHORT).show();
            }
        });

        verileriYukle(); // En sonda çağırılıyor
        return view;
    }

    private void gizleForm() {
        spinnerYatirimTuru.setVisibility(View.GONE);
        textViewYatirimTuruLabel.setVisibility(View.GONE);
        editTextYatirimIsmi.setVisibility(View.GONE);
        editTextAdet.setVisibility(View.GONE);
        editTextBirimFiyat.setVisibility(View.GONE);
        buttonHesapla.setVisibility(View.GONE);
        gizleAlanlar(); // Alt formları da gizle
    }

    private void gizleAlanlar() {
        layoutDoviz.setVisibility(View.GONE);
        layoutMaden.setVisibility(View.GONE);
        layoutCoin.setVisibility(View.GONE);
        layoutHisse.setVisibility(View.GONE);
    }

    private void verileriYukle() {
        yatirimListesi.clear();
        yatirimListesi.addAll(yatirimDao.tumCoinleriGetir(aktifKullanici));
        yatirimListesi.addAll(yatirimDao.tumBorsalariGetir(aktifKullanici));
        yatirimListesi.addAll(yatirimDao.tumDovizleriGetir(aktifKullanici));
        yatirimListesi.addAll(yatirimDao.tumMadenleriGetir(aktifKullanici));
        adapter.notifyDataSetChanged();
        guncelleToplamTutar();
    }

    private void guncelleToplamTutar() {
        double toplam = 0;
        for (Yatirim y : yatirimListesi) {
            toplam += y.yatirimTutariHesapla();
        }
        textViewToplamTutar.setText(String.format("Toplam Tutar: %.2f ₺", toplam));
    }

    @Override
    public void onSilClick(int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.yatirimi_sil)
                .setMessage(R.string.yatirimi_silmek_istediginize_emin_misiniz)
                .setPositiveButton(R.string.evet, (dialog, which) -> {
                    // Kullanıcı "Evet" derse silme işlemini gerçekleştir
                    Yatirim silinecek = yatirimListesi.get(position);
                    yatirimDao.yatirimSil(silinecek, aktifKullanici.getUserName());
                    yatirimListesi.remove(position);
                    adapter.notifyItemRemoved(position);
                    guncelleToplamTutar();
                    Toast.makeText(requireContext(), R.string.yatirim_silindi, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.hayir, (dialog, which) -> {
                    // Kullanıcı "Hayır" derse hiçbir şey yapma, dialog kapanır
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // İsteğe bağlı uyarı ikonu
                .show();
    }

    @Override
    public void onDuzenleClick(int position) {
        Toast.makeText(requireContext(), R.string.duzenle_ozelligi_henuz_aktif_degil, Toast.LENGTH_SHORT).show();
    }
}