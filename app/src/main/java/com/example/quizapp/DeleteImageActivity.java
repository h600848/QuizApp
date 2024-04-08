package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizapp.viewmodel.ImageViewModel;

public class DeleteImageActivity extends AppCompatActivity {
    private int imageId = -1; // For å lagre bildets ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_image);

        imageId = getIntent().getIntExtra("IMAGE_ID", -1); // Henter bildets ID
        String name = getIntent().getStringExtra("NAME");
        String imageUriString = getIntent().getStringExtra("IMAGE_URI");
        Uri imageUri = Uri.parse(imageUriString);

        TextView textView = findViewById(R.id.textView_delete_picture);
        ImageView imageView = findViewById(R.id.imageView_delete_picture);

        textView.setText(name);
        imageView.setImageURI(imageUri);
    }

    public void deleteButton(View view){
        // Bruker AlertDialog.Builder for å bygge og vise en dialogboks til brukeren
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete picture");
        builder.setMessage("Are you sure you want to delete the picture?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            if (imageId != -1) {
                ImageViewModel imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
                imageViewModel.deleteImageById(imageId);
                finish(); // Lukker aktiviteten og returnerer til forrige skjerm
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}