package com.example.saklasamani.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.saklasamani.model.Harcama;

import java.util.ArrayList;
import java.util.List;

public class HarcamaDao {

    private DatabaseHelper dbHelper;

    public HarcamaDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addHarcama(String userName, double amount, String category, String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userName", userName);
        values.put("amount", amount);
        values.put("category", category); // yeni alan
        values.put("note", note);


        db.insert("harcama", null, values);
        db.close();
    }


    public boolean deleteHarcama(String userName, String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("harcama", "userName = ? AND note = ?", new String[]{userName, note});
        db.close();
        return rows > 0;
    }

    public List<Harcama> getHarcamalar(String userName) {
        List<Harcama> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("harcama",
                new String[]{"amount", "category","note"},
                "userName = ?",
                new String[]{userName},
                null, null, null);

        while (cursor.moveToNext()) {
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String category= cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
            list.add(new Harcama(amount, category, note));
        }

        cursor.close();
        db.close();
        return list;
    }
}
