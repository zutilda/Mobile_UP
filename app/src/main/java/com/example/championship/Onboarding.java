package com.example.championship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;



@SuppressLint("NonConstantResourceId")
public class Onboarding extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        findViewById(R.id.ButtonEntry).setOnClickListener(this);
        findViewById(R.id.TVRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ButtonEntry:
                startActivity(new Intent(Onboarding.this,
                        Login.class));
                break;

            case R.id.TVRegister:
                startActivity(new Intent(Onboarding.this,
                        Register.class));
                break;
        }
    }
}