package com.example.saklasamani.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.saklasamani.data.DatabaseHelper;
import com.example.saklasamani.model.yatirim.Yatirim;
import com.example.saklasamani.model.yatirim.Borsa;
import com.example.saklasamani.model.yatirim.Coin;
import com.example.saklasamani.model.yatirim.DegerliMaden;
import com.example.saklasamani.model.yatirim.Doviz;

import java.util.ArrayList;
import java.util.List;

public class YatirimManager {
    private static YatirimManager instance;
    private final DatabaseHelper dbHelper;
    private final String currentUser;

    private YatirimManager(Context context, String currentUser) {
        this.dbHelper = new DatabaseHelper(context.getApplicationContext());
        this.currentUser = currentUser;
    }

    public static YatirimManager getInstance(Context context, String currentUser) {
        if (instance == null) {
            instance = new YatirimManager(context, currentUser);
        }
        return instance;
    }


    public void yatirimEkle(Yatirim yatirim) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put("user_id", yatirim.getUserId());
        values.put("userName", currentUser);
        values.put("amount", yatirim.getYatirimTutari());
        values.put("yatirimIsmi", yatirim.getYatirimIsmi());
        values.put("yatirimAdeti", yatirim.getYatirimAdeti());
        values.put("yatirimBirimFiyati", yatirim.getYatirimBirimFiyati());
        values.put("yatirimTuru", yatirim.getYatirimTuru());

        // Alt sınıf özel alanlar
        if (yatirim instanceof Doviz) {
            Doviz d = (Doviz) yatirim;
            values.put("dovizCinsi", d.getDovizCinsi());
        } else if (yatirim instanceof DegerliMaden) {
            DegerliMaden m = (DegerliMaden) yatirim;
            values.put("madenTuru", m.getMadenTuru());
        } else if (yatirim instanceof Borsa) {
            Borsa h = (Borsa) yatirim;
            values.put("sirketAdi", h.getSirketAdi());
            values.put("hisseSembolu", h.getSembol());
        } else if (yatirim instanceof Coin) {
            Coin k = (Coin) yatirim;
            values.put("coinSembolu", k.getCoinSembol());
            values.put("coinTipi", k.getCoinTipi());
        }

        db.insert("invest", null, values);
        db.close();
    }


    public void yatirimSil(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("invest", "id=? AND userName=?", new String[]{String.valueOf(id), currentUser});
        db.close();
    }

    public List<Yatirim> getYatirimListesi() {
        List<Yatirim> liste = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("invest", null, "userName=?", new String[]{currentUser}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String tur = cursor.getString(cursor.getColumnIndexOrThrow("yatirimTuru"));
                Yatirim y = null;

                switch (tur) {
                    case "Doviz":
                        y = new Doviz(
                                cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                                cursor.getString(cursor.getColumnIndexOrThrow("dovizCinsi"))
                        );
                        break;

                    case "DegerliMaden":
                        y = new DegerliMaden(
                                cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                                cursor.getString(cursor.getColumnIndexOrThrow("madenTuru"))
                        );
                        break;

                    case "Borsa":
                        y = new Borsa(
                                cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                                cursor.getString(cursor.getColumnIndexOrThrow("sirketAdi")),
                                cursor.getString(cursor.getColumnIndexOrThrow("hisseSembolu"))
                        );
                        break;

                    case "Coin":
                        y = new Coin(
                                cursor.getString(cursor.getColumnIndexOrThrow("yatirimIsmi")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimAdeti")),
                                cursor.getDouble(cursor.getColumnIndexOrThrow("yatirimBirimFiyati")),
                                cursor.getString(cursor.getColumnIndexOrThrow("coinSembolu")),
                                cursor.getString(cursor.getColumnIndexOrThrow("coinTipi"))
                        );
                        break;
                }

                if (y != null) {
                    //y.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    // Eğer note varsa ve setNote() varsa:
                    if (cursor.getColumnIndex("note") != -1) {
                        // NOT: setNote() Yatirim'da veya interface'te tanımlı olmalı.
                        // y.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
                    }
                    liste.add(y);
                }

            } while (cursor.moveToNext());
            Log.d("YATIRIM_DB_KONTROL", "Toplam kayıt: " + cursor.getCount());

            while (cursor.moveToNext()) {
                String isim = cursor.getString(cursor.getColumnIndexOrThrow("isim"));
                String tur = cursor.getString(cursor.getColumnIndexOrThrow("yatirimTuru"));
                Log.d("YATIRIM_DB_KONTROL", "Yatırım: " + isim + " - Tür: " + tur);
            }
        }

        cursor.close();
        db.close();
        return liste;
    }

    public double toplamYatirimTutari() {
        double toplam = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM invest WHERE userName = ?", new String[]{currentUser});
        if (cursor.moveToFirst()) {
            toplam = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return toplam;
    }
    public void yatirimGuncelle(int id, Yatirim yatirim) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("yatirimIsmi", yatirim.getYatirimIsmi());
        values.put("yatirimAdeti", yatirim.getYatirimAdeti());
        values.put("yatirimBirimFiyati", yatirim.getYatirimBirimFiyati());
        values.put("yatirimTuru", yatirim.getYatirimTuru()); // Eğer güncellenecekse

        // Alt sınıf bilgileri
        if (yatirim instanceof Doviz) {
            values.put("dovizCinsi", ((Doviz) yatirim).getDovizCinsi());
        } else if (yatirim instanceof DegerliMaden) {
            values.put("madenTuru", ((DegerliMaden) yatirim).getMadenTuru());
        } else if (yatirim instanceof Borsa) {
            values.put("sirketAdi", ((Borsa) yatirim).getSirketAdi());
            values.put("hisseSembolu", ((Borsa) yatirim).getSembol());
        } else if (yatirim instanceof Coin) {
            values.put("coinSembolu", ((Coin) yatirim).getCoinSembol());
            values.put("coinTipi", ((Coin) yatirim).getCoinTipi());
        }

        db.update("invest", values, "id=? AND userName=?", new String[]{String.valueOf(id), currentUser});
        db.close();
    }


}
