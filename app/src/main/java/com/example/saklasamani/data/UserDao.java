package com.example.saklasamani.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.saklasamani.model.User;

public class UserDao {

    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName", user.getUserName());
        values.put("password", user.getPassword());
        values.put("income", user.getIncome());
        values.put("budget", user.getBudget());

        long result = db.insert("user", null, values);
        db.close();
        return result;
    }

    public User login(String userName, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"userName", "password", "income", "budget"},
                "userName = ? AND password = ?",
                new String[]{userName, password},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            double income = cursor.getDouble(cursor.getColumnIndexOrThrow("income"));
            double budget = cursor.getDouble(cursor.getColumnIndexOrThrow("budget"));
            user = new User(userName, password, income, budget);
        }

        cursor.close();
        db.close();
        return user;
    }

    public boolean updateIncome(String userName, double newIncome) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("income", newIncome);
        int rows = db.update("user", values, "userName = ?", new String[]{userName});
        db.close();
        return rows > 0;
    }

    public boolean updateBudget(String userName, double newBudget) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("budget", newBudget);
        int rows = db.update("user", values, "userName = ?", new String[]{userName});
        db.close();
        return rows > 0;
    }

    public boolean deleteUser(String userName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("user", "userName = ?", new String[]{userName});
        db.close();
        return rows > 0;
    }

    public User getUserByUserName(String userName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"userName", "password", "income", "budget"},
                "userName = ?",
                new String[]{userName},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            double income = cursor.getDouble(cursor.getColumnIndexOrThrow("income"));
            double budget = cursor.getDouble(cursor.getColumnIndexOrThrow("budget"));
            user = new User(userName, password, income, budget);
        }

        cursor.close();
        db.close();
        return user;
    }
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"id", "userName", "password", "income", "budget"},
                "id = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            String userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            double income = cursor.getDouble(cursor.getColumnIndexOrThrow("income"));
            double budget = cursor.getDouble(cursor.getColumnIndexOrThrow("budget"));
            user = new User(userId, userName, password, income, budget);
        }

        cursor.close();
        db.close();
        return user;
    }


    public boolean decreaseBudget(int userId, double amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("user", new String[]{"budget"}, "id = ?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor.moveToFirst()) {
            double currentBudget = cursor.getDouble(cursor.getColumnIndexOrThrow("budget"));
            double newBudget = currentBudget - amount;

            ContentValues values = new ContentValues();
            values.put("budget", newBudget);

            int rows = db.update("user", values, "id = ?", new String[]{String.valueOf(userId)});
            cursor.close();
            db.close();
            return rows > 0;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

}
