package com.example.saklasamani.manager;

import com.example.saklasamani.model.yatirim.Yatirim;

import java.util.ArrayList;
import java.util.List;
public class YatirimManager {
    private static YatirimManager instance;
    private List<Yatirim> yatirimListesi;

    public YatirimManager() {
        this.yatirimListesi = new ArrayList<>();
    }
    public static YatirimManager getInstance() {
        if (instance == null) {
            instance = new YatirimManager();
        }
        return instance;
    }

    public void yatirimEkle(Yatirim yatirim) {
        yatirimListesi.add(yatirim);
    }

    public void yatirimSil(int position) {
        if (position >= 0 && position < yatirimListesi.size()) {
            yatirimListesi.remove(position);
        }
    }
    public double toplamYatirimTutari() {
        double toplam = 0;
        for (Yatirim y : yatirimListesi) {
            toplam += y.yatirimTutariHesapla();
        }
        return toplam;
    }

    public List<Yatirim> getYatirimListesi() {
        return yatirimListesi;
    }
}