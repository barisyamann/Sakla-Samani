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

/*public class YatirimManager {

    private List<Yatirim> yatirimListesi;

    public YatirimManager() {
        this.yatirimListesi = new ArrayList<>();
    }

    // Yatırım ekleme
    public void yatirimEkle(Yatirim yatirim) {
        yatirimListesi.add(yatirim);
    }

    // Tüm yatırımları listeleme (şimdilik gerek yok ama dursun)
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
}*/