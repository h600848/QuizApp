package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (GalleryDataManager.getInstance().getGalleryItems().isEmpty()) {
            initializeGalleryItems();
        }

        Button galleryButton = findViewById(R.id.galleryButton);
        Button quizButton = findViewById(R.id.quizButton);

        galleryButton.setOnClickListener(v -> startGalleryActivity());
        quizButton.setOnClickListener(v -> startQuizActivity());
    }

    private void startGalleryActivity() {
        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
        startActivity(intent);
    }

    private void startQuizActivity() {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        startActivity(intent);
    }

    private void initializeGalleryItems() {
        ArrayList<GalleryItem> galleryItems = new ArrayList<>();
        galleryItems.add(new GalleryItem("Gorilla", R.drawable.gorilla));
        galleryItems.add(new GalleryItem("Isbjørn", R.drawable.isbjorn));
        galleryItems.add(new GalleryItem("Hai", R.drawable.hai));
        galleryItems.add(new GalleryItem("Ekorn", R.drawable.ekorn));
        galleryItems.add(new GalleryItem("Elg", R.drawable.elg));
        galleryItems.add(new GalleryItem("Krokodille", R.drawable.krokodille));

        // Setter startbildene i GalleryDataManager
        GalleryDataManager.getInstance().setGalleryItems(galleryItems);
    }
}