package com.example.saklasamani.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.saklasamani.model.User;
import com.example.saklasamani.model.yatirim.Borsa;
import com.example.saklasamani.model.yatirim.Coin;
import com.example.saklasamani.model.yatirim.DegerliMaden;
import com.example.saklasamani.model.yatirim.Doviz;
import com.example.saklasamani.model.yatirim.Yatirim;

import java.util.ArrayList;
import java.util.List;

public class YatirimDao {

    //private SQLiteDatabase db;
    private DatabaseHelper dbHelper; // DatabaseHelper nesnesini sınıf seviyesinde tutuyoruz

    public YatirimDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // --- COIN EKLE ---
    public void addCoin(Coin coin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("userName", coin.getUserName());
            values.put("yatirimIsmi", coin.getYatirimIsmi());
            values.put("coinSembol", coin.getCoinSembol());
            values.put("coinTipi", coin.getCoinTipi());
            values.put("yatirimAdeti", coin.getYatirimAdeti());
            values.put("yatirimBirimFiyati", coin.getYatirimBirimFiyati());
            values.put("amount", coin.yatirimTutariHesapla()); // amount'u hesaplayarak ekliyoruz
            db.insert("coin", null, values);
        }finally{
            db.close();
        }

    }

    // --- TÜM COINLERİ GETİR ---
    public List<Coin> getCoins(String userName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Coin> coinList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM coin WHERE userName = ?", new String[]{userName});

        while (cursor.moveToNext()) {
            Coin coin = new Coin(
                    cursor.getString(cursor.getColumnIndexOrThrow("userName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                    cursor.getString(cursor.getColumnIndexOrThrow("coinSembol")),
                    cursor.getString(cursor.getColumnIndexOrThrow("coinTipi"))
            );
            coinList.add(coin);
        }
        cursor.close();
        return coinList;
    }

    // --- BORSA EKLE ---
    public void addBorsa(Borsa borsa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("userName", borsa.getUserName());
            values.put("yatirimIsmi", borsa.getYatirimIsmi());
            values.put("sirketAdi", borsa.getSirketAdi());
            values.put("sembol", borsa.getSembol());
            values.put("yatirimAdeti", borsa.getYatirimAdeti());
            values.put("yatirimBirimFiyati", borsa.getYatirimBirimFiyati());
            values.put("amount", borsa.yatirimTutariHesapla());
            db.insert("borsa", null, values);
        } finally {
            db.close();
        }
    }
    public List<Yatirim> tumYatirimlariGetir(User user) {
        List<Yatirim> tumYatirimlar = new ArrayList<>();
        tumYatirimlar.addAll(tumCoinleriGetir(user));
        tumYatirimlar.addAll(tumBorsalariGetir(user));
        tumYatirimlar.addAll(tumDovizleriGetir(user));
        tumYatirimlar.addAll(tumMadenleriGetir(user));
        return tumYatirimlar;
    }


    // --- TÜM BORSALARI GETİR ---
    public List<Borsa> getBorsalar(String userName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Borsa> borsaList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM borsa WHERE userName = ?", new String[]{userName});

        while (cursor.moveToNext()) {
            Borsa borsa = new Borsa(
                    cursor.getString(cursor.getColumnIndexOrThrow("userName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sirketAdi")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sembol"))
            );
            borsaList.add(borsa);
        }
        cursor.close();
        return borsaList;
    }

    // --- DÖVİZ EKLE ---
    public void addDoviz(Doviz doviz) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("userName", doviz.getUserName());
            values.put("yatirimIsmi", doviz.getYatirimIsmi());
            values.put("dovizCinsi", doviz.getDovizCinsi());
            values.put("yatirimAdeti", doviz.getYatirimAdeti());
            values.put("yatirimBirimFiyati", doviz.getYatirimBirimFiyati());
            values.put("amount", doviz.yatirimTutariHesapla()); // amount'u hesaplayarak ekliyoruz
            db.insert("doviz", null, values);
        }finally{
            db.close();
        }

    }

    // --- TÜM DÖVİZLERİ GETİR ---
    public List<Doviz> getDovizler(String userName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Doviz> dovizList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM doviz WHERE userName = ?", new String[]{userName});

        while (cursor.moveToNext()) {
            Doviz doviz = new Doviz(
                    cursor.getString(cursor.getColumnIndexOrThrow("userName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dovizCinsi"))
            );
            dovizList.add(doviz);
        }
        cursor.close();
        return dovizList;
    }

    // --- DEĞERLİ MADEN EKLE ---
    public void addDegerliMaden(DegerliMaden degerliMaden) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("userName", degerliMaden.getUserName());
            values.put("yatirimIsmi", degerliMaden.getYatirimIsmi());
            values.put("madenTuru", degerliMaden.getMadenTuru());
            values.put("yatirimAdeti", degerliMaden.getYatirimAdeti());
            values.put("yatirimBirimFiyati", degerliMaden.getYatirimBirimFiyati());
            values.put("amount", degerliMaden.yatirimTutariHesapla()); // amount'u hesaplayarak ekliyoruz
            db.insert("degerli_maden", null, values);
        }finally{
            db.close();
        }

    }

    // --- TÜM DEĞERLİ MADENLERİ GETİR ---
    public List<DegerliMaden> getDegerliMadenler(String userName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<DegerliMaden> degerliMadenList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM degerli_maden WHERE userName = ?", new String[]{userName});

        while (cursor.moveToNext()) {
            DegerliMaden degerliMaden = new DegerliMaden(
                    cursor.getString(cursor.getColumnIndexOrThrow("userName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                    cursor.getString(cursor.getColumnIndexOrThrow("madenTuru"))
            );
            degerliMadenList.add(degerliMaden);
        }
        cursor.close();
        return degerliMadenList;
    }

    public double yatirimSil(Yatirim yatirim, String userName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        double silinenTutar = 0;

        if (yatirim instanceof Coin) {
            Cursor cursor = db.rawQuery("SELECT yatirimAdeti, yatirimBirimFiyati FROM coin WHERE yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
            if (cursor.moveToFirst()) {
                double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
                double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
                silinenTutar = adet * birimFiyat;
            }
            cursor.close();
            db.delete("coin", "yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
        } else if (yatirim instanceof Borsa) {
            Cursor cursor = db.rawQuery("SELECT yatirimAdeti, yatirimBirimFiyati FROM borsa WHERE yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
            if (cursor.moveToFirst()) {
                double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
                double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
                silinenTutar = adet * birimFiyat;
            }
            cursor.close();
            db.delete("borsa", "yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
        } else if (yatirim instanceof Doviz) {
            Cursor cursor = db.rawQuery("SELECT yatirimAdeti, yatirimBirimFiyati FROM doviz WHERE yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
            if (cursor.moveToFirst()) {
                double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
                double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
                silinenTutar = adet * birimFiyat;
            }
            cursor.close();
            db.delete("doviz", "yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
        } else if (yatirim instanceof DegerliMaden) {
            Cursor cursor = db.rawQuery("SELECT yatirimAdeti, yatirimBirimFiyati FROM degerli_maden WHERE yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
            if (cursor.moveToFirst()) {
                double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
                double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
                silinenTutar = adet * birimFiyat;
            }
            cursor.close();
            db.delete("degerli_maden", "yatirimIsmi = ? AND userName = ?",
                    new String[]{yatirim.getYatirimIsmi(), userName});
        }
        Log.d("Silme", "Siliniyor: " + yatirim.getYatirimIsmi() + ", kullanıcı: " + userName + ", tutar: " + silinenTutar);
        db.close();
        return silinenTutar;
    }
    public List<Yatirim> tumCoinleriGetir(User user) {
        List<Yatirim> liste = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM coin WHERE userName = ?", new String[]{user.getUserName()});

        while (cursor.moveToNext()) {
            String isim = cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi"));
            double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
            double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
            String sembol = cursor.getString(cursor.getColumnIndexOrThrow("coinSembol"));
            String coinTipi = cursor.getString(cursor.getColumnIndexOrThrow("coinTipi"));

            liste.add(new Coin(user.getUserName(), isim, adet, birimFiyat, sembol, coinTipi));
        }

        cursor.close();
        db.close();
        return liste;
    }

    public List<Yatirim> tumBorsalariGetir(User user) {
        List<Yatirim> liste = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM borsa WHERE userName = ?", new String[]{user.getUserName()});

        while (cursor.moveToNext()) {
            String isim = cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi"));
            double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
            double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
            String sirketAdi = cursor.getString(cursor.getColumnIndexOrThrow("sirketAdi"));
            String sembol = cursor.getString(cursor.getColumnIndexOrThrow("sembol"));

            liste.add(new Borsa(user.getUserName(), isim, adet, birimFiyat, sirketAdi, sembol));
        }

        cursor.close();
        db.close();
        return liste;
    }

    public List<Yatirim> tumDovizleriGetir(User user) {
        List<Yatirim> liste = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM doviz WHERE userName = ?", new String[]{user.getUserName()});

        while (cursor.moveToNext()) {
            String isim = cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi"));
            double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
            double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
            String dovizCinsi = cursor.getString(cursor.getColumnIndexOrThrow("dovizCinsi"));

            liste.add(new Doviz(user.getUserName(), isim, adet, birimFiyat, dovizCinsi));
        }

        cursor.close();
        db.close();
        return liste;
    }

    public List<Yatirim> tumMadenleriGetir(User user) {
        List<Yatirim> liste = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM degerli_maden WHERE userName = ?", new String[]{user.getUserName()});

        while (cursor.moveToNext()) {
            String isim = cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi"));
            double adet = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti"));
            double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati"));
            String madenTuru = cursor.getString(cursor.getColumnIndexOrThrow("madenTuru"));

            liste.add(new DegerliMaden(user.getUserName(), isim, adet, birimFiyat, madenTuru));
        }

        cursor.close();
        db.close();
        return liste;
    }



}