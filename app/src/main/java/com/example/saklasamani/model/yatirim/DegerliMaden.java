package com.example.saklasamani.model.yatirim;

public class DegerliMaden extends Yatirim {
    private String madenTuru;

    public DegerliMaden(String userName, String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String madenTuru) {
        super(userName, yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.madenTuru = madenTuru;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Yatırım Türü: Değerli Maden");
        System.out.println("Kullanıcı: " + getUserName());
        System.out.println("Maden Türü: " + madenTuru);
        System.out.println("Yatırım İsmi: " + yatirimIsmi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    @Override
    public String getYatirimTuru() {
        return "DegerliMaden";
    }

    public String getMadenTuru() {
        return madenTuru;
    }

    public void setMadenTuru(String madenTuru) {
        this.madenTuru = madenTuru;
    }
}
