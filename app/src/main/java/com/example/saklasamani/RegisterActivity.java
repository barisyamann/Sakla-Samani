package com.example.saklasamani;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserName, etPassword, etIncome, etBudget;
    private Button btnRegister, btnGoToLogin;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  // activity_register.xml kullanılıyor

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etIncome = findViewById(R.id.etIncome);
        etBudget= findViewById(R.id.etBudget);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin); // yeni buton

        userDao = new UserDao(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                double income = Double.parseDouble(etIncome.getText().toString());
                double budget= Double.parseDouble(etBudget.getText().toString());
                User user = new User(userName, password, income, budget);

                long result = userDao.registerUser(user);

                if (result != -1) {
                    Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                    // Kayıt sonrası giriş ekranına yönlendir
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Kayıt sırasında hata oluştu.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kullanıcı zaten hesabı varsa LoginActivity'e yönlendir
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
