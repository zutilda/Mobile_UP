package com.example.championship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressLint("SetTextI18n")
public class Main extends AppCompatActivity implements View.OnClickListener {

    public static Users user;
    public List<Quotes> data = new ArrayList<>();
    public List<Feelings> fill = new ArrayList<>();
    AdapterQuotes adapterQuotes;
    AdapterFeelings adapterFeelings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeComponent();
        RecyclerView recyclerView = findViewById(R.id.rvFeelings);
        ListView lvQuotes = findViewById(R.id.lvQuotes);
        adapterFeelings = new AdapterFeelings(Main.this,
                fill);
        adapterQuotes = new AdapterQuotes(this, data);

        new GetQuotes().execute();
        recyclerView.setAdapter(adapterFeelings);
        new GetFeel().execute();
        lvQuotes.setAdapter(adapterQuotes);
    }

    private void InitializeComponent() {

        new GetSetBitmap(findViewById(R.id.imageAvatar)).
                execute(user.getAvatar());

        findViewById(R.id.imageMenu).setOnClickListener(this);
        findViewById(R.id.imageAvatar).setOnClickListener(this);
        findViewById(R.id.imageProfile).setOnClickListener(this);
        findViewById(R.id.imageListen).setOnClickListener(this);

        TextView TVGreeting = findViewById(R.id.TVGreeting);
        TVGreeting.setText("С возвращением, " + user.getNickName() + "!");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageMenu:
                startActivity(new Intent(Main.this, Menu.class));
                break;

            case R.id.imageAvatar:
            case R.id.imageProfile:
                startActivity(new Intent(Main.this,
                        Profile.class));
                break;

            case R.id.imageListen:
                startActivity(new Intent(Main.this,
                        Listen.class));
                break;
        }
    }

    private class GetFeel extends AsyncTask<Void, Void, String> { // Вывод списка ощущений

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/Feels");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception exception) {
                return null;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                fill.clear();
                adapterFeelings.notifyDataSetChanged();

                JSONObject object = new JSONObject(s);
                JSONArray tempArray = object.getJSONArray("data");

                for (int i = 0; i < tempArray.length(); i++) {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    Feelings tempProduct = new Feelings(
                            productJson.getInt("id"),
                            productJson.getString("title"),
                            productJson.getString("image"),
                            productJson.getInt("position")
                    );
                    fill.add(tempProduct);
                    adapterFeelings.notifyDataSetChanged();
                }
                fill.sort(Comparator.comparing(Feelings::getPosition));
                adapterFeelings.notifyDataSetChanged();
            } catch (Exception exception) {
                Toast.makeText(Main.this, "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetQuotes extends AsyncTask<Void, Void, String> { // Вывод списка цитат

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/quotes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                data.clear();
                adapterQuotes.notifyDataSetInvalidated();

                JSONObject object = new JSONObject(s);
                JSONArray tempArray = object.getJSONArray("data");

                for (int i = 0; i < tempArray.length(); i++) {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    Quotes tempProduct = new Quotes(
                            productJson.getInt("id"),
                            productJson.getString("title"),
                            productJson.getString("image"),
                            productJson.getString("description")
                    );
                    data.add(tempProduct);
                    adapterQuotes.notifyDataSetInvalidated();
                }

            } catch (Exception exception) {
                Toast.makeText(Main.this, "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }
}