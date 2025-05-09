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

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName, etPassword;
    private Button btnLogin, btnGoToRegister;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // activity_login.xml'i kullanıyoruz

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister); // Yeni buton

        userDao = new UserDao(this);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                User user = userDao.login(userName, password);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                    // Buraya giriş sonrası yönlendirme yapılabilir (örneğin: MainActivity)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userName", user.getUserName());
                    intent.putExtra("income", user.getIncome()); // kullanıcı adı gönderiliyor, ihtiyaç varsa
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre hatalı.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kayıt ekranına geçiş
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
