package com.example.saklasamani.data;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE user (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "income REAL, " +
                    "userName TEXT, " +
                    "password TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
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