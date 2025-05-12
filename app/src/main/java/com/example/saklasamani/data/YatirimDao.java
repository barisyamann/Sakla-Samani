package com.example.saklasamani.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log; // Hata ayıklama için Log sınıfını ekledik

import com.example.saklasamani.model.yatirim.Yatirim;
import com.example.saklasamani.model.yatirim.DegerliMaden;
import com.example.saklasamani.model.yatirim.Doviz;
import com.example.saklasamani.model.yatirim.Borsa;
import com.example.saklasamani.model.yatirim.Coin;

import java.util.ArrayList;
import java.util.List;

public class YatirimDao {

    private static final String TAG = "YatirimDao"; // Log etiketini tanımladık
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private UserDao userDao;

    // Yatirim tablosundaki sütunların isimleri
    private static final String COLUMN_ID = "yatirimId"; // Birincil anahtar için ID
    private static final String COLUMN_YATIRIM_ISMI = "yatirimIsmi";
    private static final String COLUMN_YATIRIM_ADETI = "yatirimAdeti";
    private static final String COLUMN_YATIRIM_BIRIM_FIYATI = "yatirimBirimFiyati";
    private static final String COLUMN_YATIRIM_TURU = "yatirimTuru";
    private static final String COLUMN_USER_ID = "user_id";

    // Türlere özel sütunlar (isteğe bağlı, esnekliği artırır)
    private static final String COLUMN_DOVIZ_CINSI = "dovizCinsi";
    private static final String COLUMN_MADEN_TURU = "madenTuru";
    private static final String COLUMN_SIRKET_ADI = "sirketAdi";
    private static final String COLUMN_HISSE_SEMBOLU = "hisseSembolu";
    private static final String COLUMN_COIN_SEMBOLU = "coinSembolu";
    private static final String COLUMN_COIN_TIPI = "coinTipi";

    public YatirimDao(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        userDao = new UserDao(context);
    }

    public long addYatirim(Yatirim yatirim, int userId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_YATIRIM_ISMI, yatirim.getYatirimIsmi());
        values.put(COLUMN_YATIRIM_ADETI, yatirim.getYatirimAdeti());
        values.put(COLUMN_YATIRIM_BIRIM_FIYATI, yatirim.getYatirimBirimFiyati());
        values.put(COLUMN_YATIRIM_TURU, yatirim.getClass().getSimpleName());
        values.put(COLUMN_USER_ID, userId);

        // Türe özel bilgileri ekle
        if (yatirim instanceof Doviz) {
            values.put(COLUMN_DOVIZ_CINSI, ((Doviz) yatirim).getDovizCinsi());
        } else if (yatirim instanceof DegerliMaden) {
            values.put(COLUMN_MADEN_TURU, ((DegerliMaden) yatirim).getMadenTuru());
        } else if (yatirim instanceof Borsa) {
            values.put(COLUMN_SIRKET_ADI, ((Borsa) yatirim).getSirketAdi());
            values.put(COLUMN_HISSE_SEMBOLU, ((Borsa) yatirim).getSembol());
        } else if (yatirim instanceof Coin) {
            values.put(COLUMN_COIN_SEMBOLU, ((Coin) yatirim).getCoinSembol());
            values.put(COLUMN_COIN_TIPI, ((Coin) yatirim).getCoinTipi());
        }

        long id = db.insert("yatirimlar", null, values);
        if (id != -1) {
            Log.i(TAG, "Yatırım başarıyla eklendi. ID: " + id);
           // userDao.decreaseBudget(userId, yatirim.yatirimTutariHesapla());
            return id;
        } else {
            Log.e(TAG, "Yatırım eklenirken hata oluştu.");
            return -1;
        }
    }

    public List<Yatirim> getYatirimlar(int userId) {
        List<Yatirim> yatirimListesi = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "yatirimlar",
                    null, // Tüm sütunları seç
                    COLUMN_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)},
                    null, null, null
            );

            while (cursor.moveToNext()) {
                String tur = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YATIRIM_TURU));
                String isim = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YATIRIM_ISMI));
                double adet = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_YATIRIM_ADETI));
                double birimFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_YATIRIM_BIRIM_FIYATI));

                Yatirim yatirim = null;
                switch (tur) {
                    case "DegerliMaden":
                        String madenTuru = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MADEN_TURU));
                        yatirim = new DegerliMaden(isim, adet, birimFiyat, madenTuru);
                        break;
                    case "Doviz":
                        String dovizCinsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOVIZ_CINSI));
                        yatirim = new Doviz(isim, adet, birimFiyat, dovizCinsi);
                        break;
                    case "Borsa":
                        String sirketAdi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIRKET_ADI));
                        String hisseSembolu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HISSE_SEMBOLU));
                        yatirim = new Borsa(isim, adet, birimFiyat, sirketAdi, hisseSembolu);
                        break;
                    case "Coin":
                        String coinSembolu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COIN_SEMBOLU));
                        String coinTipi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COIN_TIPI));
                        yatirim = new Coin(isim, adet, birimFiyat, coinSembolu, coinTipi);
                        break;
                    default:
                        Log.w(TAG, "Bilinmeyen yatırım türü: " + tur);
                        break;
                }

                if (yatirim != null) {
                    yatirimListesi.add(yatirim);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Yatırımlar getirilirken hata oluştu: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return yatirimListesi;
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.i(TAG, "Veritabanı bağlantısı kapatıldı.");
        }
    }
}