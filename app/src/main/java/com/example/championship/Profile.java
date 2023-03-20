package com.example.championship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
public class Profile extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int GALLERY_REQUEST = 1;
    private GridView gvImages;
    private AdapterPhotos adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeComponent();
    }

    private void initializeComponent() {

        new GetSetBitmap(findViewById(R.id.imageAvatar)).
                execute(Main.user.getAvatar());

        TextView TVName = findViewById(R.id.TVName);
        TVName.setText(Main.user.getNickName());

        gvImages = findViewById(R.id.gvImages);
        gvImages.setOnItemClickListener(this);
        findViewById(R.id.TVExit).setOnClickListener(this);
        findViewById(R.id.imageListen).setOnClickListener(this);

        setImages();
    }

    private void setImages() {

        File direct = new File(getFilesDir() + "/PersonalPhoto");
        File[] files = direct.listFiles();
        if (files != null) {
            File[] newFiles = new File[files.length + 1];
            System.arraycopy(files, 0, newFiles, 0, files.length);
            adapter = new AdapterPhotos(this, newFiles);
        } else {
            adapter = new AdapterPhotos(this, new File[1]);
        }

        gvImages.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {

            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    Uri selectedImage = imageReturnedIntent.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                selectedImage);
                        saveImageFile(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(Profile.this,
                                "Ошибка: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void saveImageFile(Bitmap bitmap) {

        String directoryString = getFilesDir() + "/PersonalPhoto/";

        File directoryFile = new File(directoryString);

        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }

        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        currentDate.setHours(currentDate.getHours() + 3);
        String time = timeFormat.format(currentDate);
        time = time.replace(':', '-');

        File file = new File(directoryString + System.currentTimeMillis() + time + ".jpg");

        try {
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            setImages();

            Toast.makeText(Profile.this, "Фото успешно добавлено",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(Profile.this, "Ошибка: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.TVExit:
                saveEmail();
                startActivity(new Intent(Profile.this,
                        Login.class));
                break;

            case R.id.imageListen:
                startActivity(new Intent(Profile.this,
                        Listen.class));
                break;
        }
    }

    private void saveEmail() {

        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(Profile.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("alreadyLogin", false);
        editor.putString("email", Main.user.getEmail());
        editor.apply();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (id == gvImages.getCount() - 1) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        } else {
            Intent intent = new Intent(Profile.this, Photo.class);
            intent.putExtra("fileName", adapter.getItem(position).toString());
            startActivity(intent);
        }
    }
}