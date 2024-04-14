package com.example.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.adapter.RecyclerViewAdapter;
import com.example.quizapp.adapter.RecyclerViewInterface;
import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

import java.util.Comparator;

/**
 * Aktiviteten for å vise et bildegalleri. Denne klassen håndterer visning og interaksjon med en liste over bilder
 * hvor brukeren kan se, sortere, slette eller legge til nye bilder.
 * Aktiviteten bruker LiveData og ViewModel for å håndtere data på en livssyklus-bevisst måte,
 * noe som sikrer at UI-komponentene reagerer riktig på endringer i underliggende databasemodeller.
 */
public class GalleryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private ImageViewModel imageViewModel; // ViewModel som inneholder logikk for databehandling og interaksjon med databasen
    private boolean sorted = true;
    private RecyclerView recyclerView; // Visningskomponent for å liste bilder i en scrollbar liste
    private RecyclerViewAdapter recyclerViewAdapter; // Adapter som binder bildeinformasjon til visningskomponenter i RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        setupView();
        observeSetup();
    }

    /**
     * Initialiserer og konfigurerer observasjon av bilde-data fra ViewModel.
     * Denne metoden setter opp en LiveData observatør som reagerer på endringer i listen av bilder.
     * Hvis listen er tom ved første kjøring, legges det til tre standardbilder i databasen.
     * Deretter oppdateres RecyclerView-adapteren med de aktuelle bildene for å vise dem i brukergrensesnittet.
     *
     * Bruker .observe metoden for å abonnere på endringer i en liste av bilde-entiteter hentet fra en ViewModel.
     */
    private void observeSetup() {
        imageViewModel.getAllImages().observe(this, imageEntities -> {
            if (imageEntities.isEmpty()) {
                // Legger til standardbilder om ingen bilder finnes fra før
                imageViewModel.insertImage(new ImageEntity("Gorilla", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.gorilla)));
                imageViewModel.insertImage(new ImageEntity("Polar bear", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.isbjorn)));
                imageViewModel.insertImage(new ImageEntity("Fox", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.fox)));
            }

            // Oppdaterer adapteren med nye data og informerer RecyclerView om å oppdatere visningen
            recyclerViewAdapter = new RecyclerViewAdapter(this, imageEntities, this);
            recyclerViewAdapter.setImages(imageEntities);
            recyclerView.setAdapter(recyclerViewAdapter);
        });
    }

    /**
     * Initialiserer RecyclerView og setter opp dens layout manager og adapter.
     * Denne metoden sørger for at RecyclerView er klar til å vise elementer så snart de er tilgjengelige.
     */
    private void setupView() {
        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Setter layout manager som definerer hvordan elementene ordnes visuelt
    }

    /**
     * Håndterer brukerinteraksjon med sorteringsknappen for å veksle mellom sortering A-Z og Z-A.
     * Denne metoden observerer bildelisten kontinuerlig og oppdaterer sorteringen basert på brukerens valg.
     * @param view Visningsobjektet som mottok klikkeventen.
     */
    public void sortButton(View view) {
        // Refresh the RecyclerView
        refreshRecyclerView();

        Button sortButton = (Button) findViewById(R.id.sort_btn);

        imageViewModel.getAllImages().observe(this, imageList -> {

            if (!imageList.isEmpty()) {
                // Sorterer A til Z
                if (sorted) {
                    imageList.sort(Comparator.comparing(ImageEntity::getImageName));
                    sortButton.setText("Sort Z-A");
                } else {
                    imageList.sort(Comparator.comparing(ImageEntity::getImageName).reversed());
                    sortButton.setText("Sort A-Z");
                }
            }
            sorted = !sorted;
            recyclerView.setAdapter(recyclerViewAdapter);
        });
    }

    /**
     * Oppdaterer visningen i RecyclerView ved å varsle adapteren om endringer.
     * Dette sikrer at alle visuelle representasjoner av dataene er oppdaterte.
     */
    private void refreshRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Informerer adapteren om at dataene den viser har endret seg
        }
    }

    /**
     * Starter aktiviteten for å legge til et nytt bilde.
     * @param view Visningsobjektet som mottok klikkeventen.
     */
    public void addButton(View view) {
        Intent intent = new Intent(this, AddNewImageActivity.class);
        startActivity(intent);
    }

    /**
     * Håndterer interaksjoner med bildeelementer i listen.
     * @param imageId ID-en til det klikkede bildet, brukes til å hente detaljert informasjon.
     */
    @Override
    public void onItemClick(int imageId) {
        imageViewModel.getImageById(imageId).observe(this, imageEntity -> {
            if (imageEntity != null) {
                // Starter DeleteImageActivity med bildeinformasjon
                Intent intent = new Intent(this, DeleteImageActivity.class);
                intent.putExtra("NAME", imageEntity.getImageName());
                Uri imageUri = Uri.parse(imageEntity.getImagePath().toString());
                intent.putExtra("IMAGE_URI", imageUri.toString());
                intent.putExtra("IMAGE_ID", imageEntity.getImageId()); // Legger til IMAGE_ID
                startActivity(intent);
            }
        });
    }
}
