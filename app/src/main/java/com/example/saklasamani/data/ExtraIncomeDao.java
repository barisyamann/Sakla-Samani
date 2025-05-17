package com.example.saklasamani.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.saklasamani.model.ExtraIncome;

import java.util.ArrayList;
import java.util.List;

public class ExtraIncomeDao {

    private DatabaseHelper dbHelper;

    public ExtraIncomeDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addExtraIncome(String userName, double amount, String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userName", userName);
        values.put("amount", amount);
        values.put("note", note);

        db.insert("extra_income", null, values);
        db.close();
    }

    public boolean deleteExtraIncome(String userName, String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("extra_income", "userName = ? AND note = ?", new String[]{userName, note});
        db.close();
        return rows > 0;
    }

    public List<ExtraIncome> getExtraIncomes(String userName) {
        List<ExtraIncome> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("extra_income",
                new String[]{"amount", "note"},
                "userName = ?",
                new String[]{userName},
                null, null, null);

        while (cursor.moveToNext()) {
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
            list.add(new ExtraIncome(amount, note));
        }

        cursor.close();
        db.close();
        return list;
    }

}
