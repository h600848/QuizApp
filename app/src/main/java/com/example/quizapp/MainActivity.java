package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

public class MainActivity extends AppCompatActivity {

    private ImageViewModel imageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        Button galleryButton = findViewById(R.id.gallery_btn);
        Button quizButton = findViewById(R.id.play_btn);

        galleryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, GalleryActivity.class));
        });

        quizButton.setOnClickListener(v -> {
            startActivity(new Intent(this, QuizActivity.class));
        });

        startPictures();
    }

    private void startPictures() {
        imageViewModel.getAllImages().observe(this, imageEntities -> {
            if (imageEntities.isEmpty()) {
                imageViewModel.insertImage(new ImageEntity("Gorilla", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.gorilla)));
                imageViewModel.insertImage(new ImageEntity("Polar bear", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.isbjorn)));
                imageViewModel.insertImage(new ImageEntity("Fox", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.fox)));
            }
        });
    }
}