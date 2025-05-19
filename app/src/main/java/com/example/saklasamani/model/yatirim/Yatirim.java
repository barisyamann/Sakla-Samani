package com.example.saklasamani.model.yatirim;

public abstract class Yatirim implements YatirimInterface {
    protected String yatirimIsmi;
    protected double yatirimAdeti;
    protected double yatirimBirimFiyati;
    private String userName;

    public Yatirim(String userName, String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati) {
        this.userName = userName;
        this.yatirimIsmi = yatirimIsmi;
        this.yatirimAdeti = yatirimAdeti;
        this.yatirimBirimFiyati = yatirimBirimFiyati;
    }

    // Ortak tutar hesaplama (kullanmak istemeyen override eder)
    @Override
    public double yatirimTutariHesapla() {
        return yatirimAdeti * yatirimBirimFiyati;
    }

    public String getYatirimIsmi() {
        return yatirimIsmi;
    }

    public void setYatirimIsmi(String yatirimIsmi) {
        this.yatirimIsmi = yatirimIsmi;
    }

    public double getYatirimAdeti() {
        return yatirimAdeti;
    }

    public void setYatirimAdeti(double yatirimAdeti) {
        this.yatirimAdeti = yatirimAdeti;
    }

    public double getYatirimBirimFiyati() {
        return yatirimBirimFiyati;
    }

    public void setYatirimBirimFiyati(double yatirimBirimFiyati) {
        this.yatirimBirimFiyati = yatirimBirimFiyati;
    }

    public double getYatirimTutari() {
        return yatirimTutariHesapla();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Alt sınıflar bunu override edecek
    @Override
    public abstract String getYatirimTuru();

    @Override
    public abstract void bilgiGoster();
}
