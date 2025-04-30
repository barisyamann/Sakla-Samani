package com.example.saklasamani.data;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAppDB.db";
    private static final int DATABASE_VERSION = 1;

    // Buraya ihtiyaç duyduğun tablo CREATE cümlelerini ekle
    private static final String SQL_CREATE_TABLES =
            "CREATE TABLE IF NOT EXISTS example (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT" +
                    ");";

    private static final String SQL_DROP_TABLES =
            "DROP TABLE IF EXISTS example;";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLES);
        onCreate(db);
    }
}
/*
DatabaseHelper dbHelper = new DatabaseHelper(this);
SQLiteDatabase db = dbHelper.getWritableDatabase();
// db artık hazır; istersen hemen kapat:
db.close();
dbHelper.close();
*/