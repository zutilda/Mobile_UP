package com.example.championship;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

@SuppressLint("StaticFieldLeak")
public class GetSetBitmap extends AsyncTask<String, Void, Bitmap> {

    private ImageView image;

    public GetSetBitmap(ImageView imageView) {

        image = imageView;

    }

    public Bitmap doInBackground(String... urls) {

        String urlDisplay = urls[0];
        Bitmap bitmap = null;

        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Ошибка", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        image.setImageBitmap(result);
    }
}