package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<GalleryItem> galleryItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Initialiser galleryItems-listen her med startdata
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialiserer listen med startdata
        initializeGalleryItems();

        // Initialiserer adapteren her, etter at listen er initialisert
        adapter = new GalleryAdapter(galleryItems, this);
        recyclerView.setAdapter(adapter);

        // Initialiserer knappene
        Button addButton = findViewById(R.id.addButton);
        Button sortButton = findViewById(R.id.sortButton);

        // Setter opp klikkelytter på addButton
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Legger til en ny GalleryItem som et eksempel
                // Du kan endre dette til å åpne en dialog hvor brukeren kan legge inn data
                // Må fikse her
                // galleryItems.add(new GalleryItem("Ny Oppføring", R.drawable.example_image));
                // adapter.notifyItemInserted(galleryItems.size() - 1);
            }
        });

        // Setter opp klikkelytter på sortButton
        sortButton.setOnClickListener(new View.OnClickListener() {
            boolean isAscending = true; // Holder styr på nåværende sorteringsstatus

            @Override
            public void onClick(View view) {
                if (isAscending) {
                    Collections.sort(galleryItems, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                    sortButton.setText("Sorter Z-A");
                } else {
                    Collections.sort(galleryItems, (o1, o2) -> o2.getName().compareTo(o1.getName()));
                    sortButton.setText("Sorter A-Z");
                }
                isAscending = !isAscending;
                adapter.notifyDataSetChanged(); // Oppdaterer RecyclerView
            }
        });
    }

    private void initializeGalleryItems() {
        galleryItems.add(new GalleryItem("Gorilla", R.drawable.gorilla));
        galleryItems.add(new GalleryItem("Isbjørn", R.drawable.isbjorn));
        galleryItems.add(new GalleryItem("Hai", R.drawable.hai));
        galleryItems.add(new GalleryItem("Ekorn", R.drawable.ekorn));
        galleryItems.add(new GalleryItem("Elg", R.drawable.elg));
        galleryItems.add(new GalleryItem("Krokodille", R.drawable.krokodille));
    }
}