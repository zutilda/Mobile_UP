package com.example.championship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("MissingInflatedId")
public class Login extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.TVReg).setOnClickListener(this);
        findViewById(R.id.ButtonSignIn).setOnClickListener(this);
        findViewById(R.id.ButtonProfile).setOnClickListener(this);

        setEmail();
    }

    private void setEmail() {

        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(Login.this);

        String email = prefs.getString("email", "");
        if (!email.equals("")) {
            TextView TVEmail = findViewById(R.id.TVEmail);
            TVEmail.setText(email);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ButtonSignIn:
            case R.id.ButtonProfile:
                EditText email = findViewById(R.id.TVEmail);
                EditText password = findViewById(R.id.TVPassword);

                checkData(email.getText().toString(), password.getText().toString());

                authorization(email.getText().toString(), password.getText().toString());
                break;

            case R.id.TVReg:
                startActivity(new Intent(Login.this,
                        Register.class));
                break;
        }
    }

    private boolean checkData(String email, String password) {

        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(Login.this, "Заполните оба поля",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.contains("@")) {
            Toast.makeText(Login.this, "В логине отсутствует" +
                    "символ \"@\"", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void authorization(String email, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mskko2021.mad.hakta.pro/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Users user = new Users(email, password);

        Call<Users> call = retrofitAPI.getToken(user);
        call.enqueue(new Callback<Users>() {

            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                if (response.body() != null) {
                    Main.user.setNickName(response.body().getNickName());
                    Main.user.setAvatar(response.body().getAvatar());
                    Main.user.setEmail(response.body().getEmail());
                    saveData();

                    startActivity(new Intent(Login.this, Main.class));
                } else {
                    Toast.makeText(Login.this, "Пользователь не найден",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

                Toast.makeText(Login.this, "Ошибка: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveData() {

        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(Login.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("alreadyLogin", true);
        editor.putString("nickName", Main.user.getNickName());
        editor.putString("avatar", Main.user.getAvatar());
        editor.putString("email", Main.user.getEmail());
        editor.apply();
    }
}