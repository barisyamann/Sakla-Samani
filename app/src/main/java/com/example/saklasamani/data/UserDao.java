package com.example.saklasamani.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.saklasamani.model.ExtraIncome;
import com.example.saklasamani.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private DatabaseHelper dp;

    public UserDao(Context context) {
        dp = new DatabaseHelper(context);
    }

    public long registerUser(User user) {
        SQLiteDatabase db = dp.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userName", user.getUserName());
        values.put("password", user.getPassword());
        values.put("income", user.getIncome());

        long result = db.insert("user", null, values);
        db.close();
        return result;
    }

    public User login(String userName, String password) {
        SQLiteDatabase db = dp.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"userName", "password", "income"},
                "userName = ? AND password = ?",
                new String[]{userName, password},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            double income = cursor.getDouble(cursor.getColumnIndexOrThrow("income"));
            user = new User(userName, password, income);

            // Extra incomes da yükleniyor:
            List<ExtraIncome> extraIncomes = getExtraIncomes(userName);
            for (ExtraIncome ei : extraIncomes) {
                user.getExtraIncomes().add(ei);
            }
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean deleteUser(String userName) {
        SQLiteDatabase db = dp.getWritableDatabase();
        int rows = db.delete("user", "userName = ?", new String[]{userName});
        db.close();
        return rows > 0;
    }

    public boolean updateIncome(String userName, double newIncome) {
        SQLiteDatabase db = dp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("income", newIncome);
        int rows = db.update("user", values, "userName = ?", new String[]{userName});
        db.close();
        return rows > 0;
    }

    // ➕ ExtraIncome Ekleme
    public void addExtraIncome(String userName, double amount, String note) {
        SQLiteDatabase db = dp.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userName", userName);
        values.put("amount", amount);
        values.put("note", note);

        db.insert("extra_income", null, values);
        db.close();
    }

    // ➖ ExtraIncome Silme
    public boolean deleteExtraIncome(String userName, String note) {
        SQLiteDatabase db = dp.getWritableDatabase();
        int rows = db.delete("extra_income", "userName = ? AND note = ?", new String[]{userName, note});
        db.close();
        return rows > 0;
    }

    // 🔁 Tüm ExtraIncome'ları Al
    public List<ExtraIncome> getExtraIncomes(String userName) {
        List<ExtraIncome> list = new ArrayList<>();
        SQLiteDatabase db = dp.getReadableDatabase();

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
