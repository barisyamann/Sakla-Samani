package com.example.saklasamani.model.yatirim;

public class Coin extends Yatirim
{
    private String coinSembol;
    private String coinTipi; // örneğin "Kripto", "Stablecoin" gibi


    public Coin(String userName,String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String coinSembol, String coinTipi) {
        super(userName,yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.coinSembol = coinSembol;
        this.coinTipi = coinTipi;
    }

    @Override
    public double yatirimTutariHesapla() {
        return yatirimAdeti * yatirimBirimFiyati;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Coin Yatırımı");
        System.out.println("Coin Adı: " + yatirimIsmi);
        System.out.println("Coin Sembolü: " + coinSembol);
        System.out.println("Coin Tipi: " + coinTipi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    // Getter ve Setter
    public String getCoinSembol() {
        return coinSembol;
    }

    public void setCoinSembol(String coinSembol) {
        this.coinSembol = coinSembol;
    }

    public String getCoinTipi() {
        return coinTipi;
    }

    public void setCoinTipi(String coinTipi) {
        this.coinTipi = coinTipi;
    }
    @Override
    public String getYatirimTuru() {
        return "Coin";
    }

}
