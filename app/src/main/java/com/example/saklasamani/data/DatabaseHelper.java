package com.example.saklasamani.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "saklasamani.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE user (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "userName TEXT UNIQUE NOT NULL," +
                        "password TEXT NOT NULL," +
                        "income REAL DEFAULT 0," +
                        "budget REAL DEFAULT 0)"
        );

        db.execSQL(
                "CREATE TABLE extra_income (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "userName TEXT NOT NULL," +
                        "amount REAL NOT NULL," +
                        "note TEXT," +
                        "FOREIGN KEY(userName) REFERENCES user(userName) ON DELETE CASCADE)"
        );

        db.execSQL(
                "CREATE TABLE harcama (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "userName TEXT NOT NULL," +
                        "amount REAL NOT NULL," +
                        "note TEXT," +
                        "FOREIGN KEY(userName) REFERENCES user(userName) ON DELETE CASCADE)"
        );

        db.execSQL(
                "CREATE TABLE invest (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id INTEGER," +
                        "userName TEXT NOT NULL," +
                        "amount REAL," +
                        "note TEXT," +
                        "yatirimIsmi TEXT," +
                        "yatirimAdeti REAL," +
                        "yatirimBirimFiyati REAL," +
                        "yatirimTuru TEXT NOT NULL," + // Sınıf adını saklar (örn. "Doviz")
                        "dovizCinsi TEXT," +
                        "madenTuru TEXT," +
                        "sirketAdi TEXT," +
                        "hisseSembolu TEXT," +
                        "coinSembolu TEXT," +
                        "coinTipi TEXT," +
                        "FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS invest");
        db.execSQL("DROP TABLE IF EXISTS harcama");
        db.execSQL("DROP TABLE IF EXISTS extra_income");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
