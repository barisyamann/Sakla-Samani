package com.example.saklasamani.model.yatirim;

public class Borsa extends Yatirim {
    private String sirketAdi;
    private String sembol;

    public Borsa(String userName, String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String sirketAdi, String sembol) {
        super(userName, yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.sirketAdi = sirketAdi;
        this.sembol = sembol;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Yatırım Türü: Borsa");
        System.out.println("Kullanıcı: " + getUserName());
        System.out.println("Şirket Adı: " + sirketAdi);
        System.out.println("Sembol: " + sembol);
        System.out.println("Yatırım İsmi: " + yatirimIsmi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    @Override
    public String getYatirimTuru() {
        return "Borsa";
    }

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
}