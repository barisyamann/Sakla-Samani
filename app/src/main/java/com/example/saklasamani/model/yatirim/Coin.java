package com.example.saklasamani.model.yatirim;

public class Coin extends Yatirim {
    private String coinSembol;
    private String coinTipi; // örnek: Kripto, Stablecoin

    public Coin(String userName, String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati, String coinSembol, String coinTipi) {
        super(userName, yatirimIsmi, yatirimAdeti, yatirimBirimFiyati);
        this.coinSembol = coinSembol;
        this.coinTipi = coinTipi;
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Yatırım Türü: Coin");
        System.out.println("Kullanıcı: " + getUserName());
        System.out.println("Coin İsmi: " + yatirimIsmi);
        System.out.println("Sembol: " + coinSembol);
        System.out.println("Tip: " + coinTipi);
        System.out.println("Adet: " + yatirimAdeti);
        System.out.println("Birim Fiyat: " + yatirimBirimFiyati);
        System.out.println("Toplam Tutar: " + yatirimTutariHesapla());
    }

    @Override
    public String getYatirimTuru() {
        return "Coin";
    }

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
}
