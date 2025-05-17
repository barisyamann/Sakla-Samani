package com.example.saklasamani.model.yatirim;

public abstract class Yatirim implements YatirimInterface
{
    protected String yatirimIsmi;
    protected double yatirimAdeti;
    protected double yatirimBirimFiyati;
    private String userName;

    public Yatirim(String userName,String yatirimIsmi, double yatirimAdeti, double yatirimBirimFiyati) {
        this.userName=userName;
        this.yatirimIsmi = yatirimIsmi;
        this.yatirimAdeti = yatirimAdeti;
        this.yatirimBirimFiyati = yatirimBirimFiyati;
    }

    public abstract double yatirimTutariHesapla();

    // Getter ve Setter'lar
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
        return yatirimAdeti * yatirimBirimFiyati;
    }
    public abstract String getYatirimTuru();
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
