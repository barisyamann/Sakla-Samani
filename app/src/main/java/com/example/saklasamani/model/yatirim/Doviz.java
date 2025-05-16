package com.example.saklasamani.model.yatirim;

public class Doviz extends Yatirim {
    private String dovizCinsi;

    public Doviz(String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String dovizCinsi) {
        super(yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.dovizCinsi = dovizCinsi;
    }

    @Override
    public double yatirimTutariHesapla() {
        return yatirimAdeti * yatirimBirimFiyati;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Döviz Cinsi: " + dovizCinsi);
        System.out.println("Yatırım İsmi: " + yatirimIsmi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    // Getter ve Setter
    public String getDovizCinsi() {
        return dovizCinsi;
    }

    public void setDovizCinsi(String dovizCinsi) {
        this.dovizCinsi = dovizCinsi;
    }
    @Override
    public String getYatirimTuru() {
        return "Doviz";
    }
}