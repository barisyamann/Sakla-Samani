package com.example.saklasamani.manager;

import com.example.saklasamani.model.Yatirim;

import java.util.ArrayList;
import java.util.List;

public class YatirimManager {

    private List<Yatirim> yatirimListesi;

    public YatirimManager() {
        this.yatirimListesi = new ArrayList<>();
    }

    // Yatırım ekleme
    public void yatirimEkle(Yatirim yatirim) {
        yatirimListesi.add(yatirim);
    }

    // Tüm yatırımları listeleme
    public void tumYatirimlariGoster() {
        for (Yatirim y : yatirimListesi) {
            y.bilgiGoster();
            System.out.println("-----");
        }
    }

    // Toplam yatırım tutarını hesaplama
    public double toplamYatirimTutari() {
        double toplam = 0;
        for (Yatirim y : yatirimListesi) {
            toplam += y.yatirimTutariHesapla();
        }
        return toplam;
    }

    // Listeye erişim (UI'da gösterilecekse)
    public List<Yatirim> getYatirimListesi() {
        return yatirimListesi;
    }
}
