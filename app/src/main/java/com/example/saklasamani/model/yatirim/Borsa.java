package com.example.saklasamani.model.yatirim;

public class Borsa extends Yatirim
{
    private String sirketAdi;   // Örn: ASELSAN, THYAO, vb.
    private String sembol;      // Örn: ASELS, THYAO

    public Borsa(String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String sirketAdi, String sembol) {
        super(yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.sirketAdi = sirketAdi;
        this.sembol = sembol;
    }

    @Override
    public double yatirimTutariHesapla() {
        return yatirimAdeti * yatirimBirimFiyati;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Borsa Yatırımı");
        System.out.println("Şirket Adı: " + sirketAdi);
        System.out.println("Sembol: " + sembol);
        System.out.println("Yatırım İsmi: " + yatirimIsmi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    // Getter ve Setter
    public String getSirketAdi() {
        return sirketAdi;
    }

    public void setSirketAdi(String sirketAdi) {
        this.sirketAdi = sirketAdi;
    }

    public String getSembol() {
        return sembol;
    }

    public void setSembol(String sembol) {
        this.sembol = sembol;
    }
    @Override
    public String getYatirimTuru() {
        return "Borsa";
    }
}
