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
import com.example.saklasamani.model.User;
import com.example.saklasamani.model.yatirim.*;
import java.util.ArrayList;
import java.util.List;

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
    private EditText editTextYatirimIsmi, editTextAdet, editTextBirimFiyat;
    private Spinner spinnerDovizCinsi, spinnerMadenTuru;
    private EditText editTextCoinSembol, editTextCoinTipi;
    private EditText editTextSirketAdi, editTextSembol;
    private LinearLayout layoutDoviz, layoutMaden, layoutCoin, layoutHisse;
    private Button buttonHesapla;

    private User aktifKullanici;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yatirim, container, false);

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

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Coin", "Borsa", "Döviz", "Değerli Maden"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYatirimTuru.setAdapter(spinnerAdapter);

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
            view.findViewById(R.id.spinnerYatirimTuru).setVisibility(View.VISIBLE);
            view.findViewById(R.id.textViewYatirimTuruLabel).setVisibility(View.VISIBLE);
        });

        buttonHesapla.setOnClickListener(v -> {
            String isim = editTextYatirimIsmi.getText().toString();
            double adet = Double.parseDouble(editTextAdet.getText().toString());
            double birimFiyat = Double.parseDouble(editTextBirimFiyat.getText().toString());

            Yatirim yeniYatirim = null;

            switch (spinnerYatirimTuru.getSelectedItemPosition()) {
                case 0:
                    yeniYatirim = new Coin(
                            userName, isim, adet, birimFiyat,
                            editTextCoinSembol.getText().toString(),
                            editTextCoinTipi.getText().toString());
                    yatirimDao.addCoin((Coin) yeniYatirim);
                    break;
                case 1:
                    yeniYatirim = new Borsa(
                            userName, isim, adet, birimFiyat,
                            editTextSirketAdi.getText().toString(),
                            editTextSembol.getText().toString());
                    yatirimDao.addBorsa((Borsa) yeniYatirim);
                    break;
                case 2:
                    yeniYatirim = new Doviz(
                            userName, isim, adet, birimFiyat,
                            spinnerDovizCinsi.getSelectedItem().toString());
                    yatirimDao.addDoviz((Doviz) yeniYatirim);
                    break;
                case 3:
                    yeniYatirim = new DegerliMaden(
                            userName, isim, adet, birimFiyat,
                            spinnerMadenTuru.getSelectedItem().toString());
                    yatirimDao.addDegerliMaden((DegerliMaden) yeniYatirim);
                    break;
            }

            if (yeniYatirim != null) {
                yatirimListesi.add(yeniYatirim);
                adapter.notifyItemInserted(yatirimListesi.size() - 1);
                guncelleToplamTutar();
                Toast.makeText(requireContext(), "Yatırım eklendi!", Toast.LENGTH_SHORT).show();
            }
        });

        verileriYukle(); // En sonda çağırılıyor
        return view;
    }


    private void gizleAlanlar() {
        layoutDoviz.setVisibility(View.GONE);
        layoutMaden.setVisibility(View.GONE);
        layoutCoin.setVisibility(View.GONE);
        layoutHisse.setVisibility(View.GONE);
        editTextYatirimIsmi.setVisibility(View.GONE);
        editTextAdet.setVisibility(View.GONE);
        editTextBirimFiyat.setVisibility(View.GONE);
        buttonHesapla.setVisibility(View.GONE);
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
        Yatirim silinecek = yatirimListesi.get(position);
        yatirimDao.yatirimSil(silinecek, String.valueOf(aktifKullanici));
        yatirimListesi.remove(position);
        adapter.notifyItemRemoved(position);
        guncelleToplamTutar();
    }

    @Override
    public void onDuzenleClick(int position) {
        Toast.makeText(requireContext(), "Düzenle özelliği henüz aktif değil!", Toast.LENGTH_SHORT).show();
    }
}
