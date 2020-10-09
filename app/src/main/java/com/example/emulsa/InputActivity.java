package com.example.emulsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView selectedImageView = (ImageView) findViewById(R.id.selectedImageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            selectedImageView.setImageURI(Uri.parse(currentPhotoPath));
        }

        else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            selectedImageView.setImageURI(data.getData());
        }
    }

    public void OpenGallery(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        if (galleryIntent.resolveActivity(getPackageManager()) != null ) {
            startActivityForResult(Intent.createChooser(galleryIntent, "Selecciona una imagen"), REQUEST_IMAGE_GALLERY);
        }
    }

    public void OpenCamera(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Comprueba que la camara esta activa
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            // Crea el File donde ira la foto.
            File photoFile = null;
            try {
                photoFile = CreateImageFile();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File CreateImageFile() throws IOException {
        // Crear el nombre del fichero
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "JPEG_"+timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFilename, ".jpg", storageDir);

        // Guarda y devuelve la ruta del archivo
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}