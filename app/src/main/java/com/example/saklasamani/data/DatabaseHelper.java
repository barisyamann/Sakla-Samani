package com.example.saklasamani.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE user (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userName TEXT, " +
                    "password TEXT, " +
                    "income REAL, " +
                    "budget REAL)";

    private static final String CREATE_EXTRA_INCOME_TABLE =
            "CREATE TABLE extra_income (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userName TEXT, " +
                    "amount REAL, " +
                    "note TEXT, " +
                    "FOREIGN KEY(userName) REFERENCES user(userName) ON DELETE CASCADE)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_EXTRA_INCOME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS extra_income");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
