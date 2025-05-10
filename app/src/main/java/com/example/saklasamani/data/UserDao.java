package com.example.saklasamani.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;


import com.example.saklasamani.model.User;
public class UserDao {

    private  DatabaseHelper dp;

    public UserDao(Context context){
        dp = new DatabaseHelper(context);
    }
     public long registerUser(User user){
         SQLiteDatabase db = dp.getWritableDatabase();

         ContentValues values = new ContentValues();
         values.put("userName",user.getUserName());
         values.put("password",user.getPassword());
         values.put("income",user.getIncome());

         long result = db.insert("user",null,values);
         db.close();
         return  result;

     }

     public User login(String userName,String password){
        SQLiteDatabase db = dp.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"userName","password","income"},
                "username = ? AND password = ?",
                new String[]{userName,password},
                null ,null,null);


        User user = null;
        if(cursor.moveToFirst()){
            double income = cursor.getDouble(cursor.getColumnIndexOrThrow("income"));
            user = new User(userName, password, income);
        }
        cursor.close();
        db.close();
        return user;

     }
    public boolean deleteUser(String userName) {
        SQLiteDatabase db = dp.getWritableDatabase();
        int rows = db.delete("example", "userName = ?", new String[]{userName});
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


}
