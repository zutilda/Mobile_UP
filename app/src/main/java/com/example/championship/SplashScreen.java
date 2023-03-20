package com.example.championship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;



public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Main.user = new Users();

        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(SplashScreen.this);

        if (prefs.getBoolean("alreadyLogin", false)) {
            Main.user.setNickName(prefs.getString("nickName", ""));
            Main.user.setAvatar(prefs.getString("avatar", ""));
            Main.user.setEmail(prefs.getString("email", ""));
            new Handler().postDelayed(() -> startActivity(new Intent(
                    SplashScreen.this, Main.class)), 3500);
        } else {
            new Handler().postDelayed(() -> startActivity(
                    new Intent(SplashScreen.this,
                            Onboarding.class)), 2000);
        }
    }
}