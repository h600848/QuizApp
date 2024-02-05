package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<GalleryItem> galleryItems; // Oppretter en liste for å holde GalleryItems

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiserer GalleryItems
        initializeGalleryItems();

        // Finner knappene
        Button galleryButton = findViewById(R.id.galleryButton);
        Button quizButton = findViewById(R.id.quizButton);

        // Setter opp klikkelytter på galleryButton
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opprett en ny Intent for å starte GalleryActivity
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                // Start aktiviteten spesifisert av Intent
                startActivity(intent);
            }
        });

        // Setter opp klikkelytter på quizButton
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opprett en ny Intent for å starte QuizActivity
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                // Legger til hele listen av galleryItems i Intent
                intent.putExtra("galleryItems", galleryItems);
                // Start aktiviteten spesifisert av Intent
                startActivity(intent);
            }
        });

    }

    private void initializeGalleryItems() {
        galleryItems = new ArrayList<>();
        galleryItems.add(new GalleryItem("Gorilla", R.drawable.gorilla));
        galleryItems.add(new GalleryItem("Isbjørn", R.drawable.isbjorn));
        galleryItems.add(new GalleryItem("Hai", R.drawable.hai));
        galleryItems.add(new GalleryItem("Ekorn", R.drawable.ekorn));
        galleryItems.add(new GalleryItem("Elg", R.drawable.elg));
        galleryItems.add(new GalleryItem("Krokodille", R.drawable.krokodille));
    }
}