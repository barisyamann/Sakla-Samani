package com.example.saklasamani.model.yatirim;

public class Doviz extends Yatirim {
    private String dovizCinsi;

    public Doviz(String userName, String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String dovizCinsi) {
        super(userName, yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.dovizCinsi = dovizCinsi;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Yatırım Türü: Döviz");
        System.out.println("Kullanıcı: " + getUserName());
        System.out.println("Döviz Cinsi: " + dovizCinsi);
        System.out.println("Yatırım İsmi: " + yatirimIsmi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    @Override
    public String getYatirimTuru() {
        return "Doviz";
    }

    public String getDovizCinsi() {
        return dovizCinsi;
    }

    public void setDovizCinsi(String dovizCinsi) {
        this.dovizCinsi = dovizCinsi;
    }
}