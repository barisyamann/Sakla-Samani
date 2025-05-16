package com.example.saklasamani.model.yatirim;

public class DegerliMaden extends Yatirim
{
    private String madenTuru;

    public DegerliMaden(String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String madenTuru) {
        super(yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.madenTuru = madenTuru;
    }

    @Override
    public double yatirimTutariHesapla() {
        return yatirimAdeti * yatirimBirimFiyati;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Maden Türü: " + madenTuru);
        System.out.println("Yatırım İsmi: " + yatirimIsmi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    // Getter ve Setter
    public String getMadenTuru() {
        return madenTuru;
    }

    public void setMadenTuru(String madenTuru) {
        this.madenTuru = madenTuru;
    }
    @Override
    public String getYatirimTuru() {
        return "Maden";
    }
}
