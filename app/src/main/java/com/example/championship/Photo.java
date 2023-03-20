package com.example.championship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import java.io.File;

@SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
public class Photo extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageView imagePhoto;
    private long currentTouchTime = 0;
    private boolean isDefaultImageSize = true;
    private boolean isDoubleClick = true;
    private String fileName;
    private GestureDetector gdt = new GestureDetector(new GestureListener());
    private int SWIPE_MIN_DISTANCE = 120;
    private int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imagePhoto = findViewById(R.id.imagePhoto);
        imagePhoto.setOnTouchListener(this);

        findViewById(R.id.TVDelete).setOnClickListener(this);
        findViewById(R.id.TVClose).setOnClickListener(this);

        fileName = getIntent().getExtras().getString("fileName");
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        imagePhoto.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.TVDelete:
                deleteImage();
                break;

            case R.id.TVClose:
                startActivity(new Intent(Photo.this, Profile.class));
                break;

            case R.id.imagePhoto:
                long lastTouchTime = currentTouchTime;
                currentTouchTime = System.currentTimeMillis();

                if (currentTouchTime - lastTouchTime <= 300) {
                    if (isDefaultImageSize) {
                        imagePhoto.getLayoutParams().width = imagePhoto.getWidth() * 2;
                        imagePhoto.getLayoutParams().height = imagePhoto.getHeight() * 2;
                        isDefaultImageSize = false;
                    } else {
                        imagePhoto.getLayoutParams().width = imagePhoto.getWidth() / 2;
                        imagePhoto.getLayoutParams().height = imagePhoto.getHeight() / 2;
                        isDefaultImageSize = true;
                    }

                    imagePhoto.requestLayout();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (isDoubleClick) {
            long lastTouchTime = currentTouchTime;
            currentTouchTime = System.currentTimeMillis();

            if (currentTouchTime - lastTouchTime <= 300) {
                if (isDefaultImageSize) {
                    imagePhoto.getLayoutParams().width = imagePhoto.getWidth() * 2;
                    imagePhoto.getLayoutParams().height = imagePhoto.getHeight() * 2;
                    isDefaultImageSize = false;
                } else {
                    imagePhoto.getLayoutParams().width = imagePhoto.getWidth() / 2;
                    imagePhoto.getLayoutParams().height = imagePhoto.getHeight() / 2;
                    isDefaultImageSize = true;
                }

                imagePhoto.requestLayout();
            }
            isDoubleClick = false;
        } else {
            isDoubleClick = true;
        }

        gdt.onTouchEvent(event);

        return true;
    }

    private void deleteImage() {

        if (new File(fileName).delete()) {
            Toast.makeText(Photo.this, "Фото успешно удалено",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Photo.this,
                    Profile.class));
        } else {
            Toast.makeText(Photo.this, "Фото не удалено",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                startActivity(new Intent(Photo.this, Profile.class));
            } else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                deleteImage();
            }
            return false;
        }
    }
}