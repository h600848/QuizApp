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

/**
 * DeleteImageActivity er designet for å håndtere sletting av bilder fra applikasjonen.
 * Denne aktiviteten viser bildet og informasjon til brukeren og tillater bekreftet sletting gjennom en dialog.
 */
public class DeleteImageActivity extends AppCompatActivity {
    private int imageId = -1; // Variabel for å lagre ID-en til bildet som kan slettes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_image);

        // Hent data som er sendt fra en annen aktivitet, inkludert bildets ID, NAVN og URI
        imageId = getIntent().getIntExtra("IMAGE_ID", -1);
        String name = getIntent().getStringExtra("NAME");
        String imageUriString = getIntent().getStringExtra("IMAGE_URI");
        Uri imageUri = Uri.parse(imageUriString);

        TextView textView = findViewById(R.id.textView_delete_picture);
        ImageView imageView = findViewById(R.id.imageView_delete_picture);

        textView.setText(name);
        imageView.setImageURI(imageUri);
    }

    /**
     * Viser en bekreftelsesdialog for å bekrefte brukerens intensjon om å slette det valgte bildet.
     * Hvis brukeren bekrefter, slettes bildet fra databasen.
     * @param view Visningsobjektet som mottok klikk-eventet, vanligvis en knapp.
     */
    public void deleteButton(View view){
        // Bruker AlertDialog.Builder for å bygge og vise en dialogboks til brukeren
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete picture");
        builder.setMessage("Are you sure you want to delete the picture?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            if (imageId != -1) {
                ImageViewModel imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
                imageViewModel.deleteImageById(imageId);
                finish(); // Avslutter aktiviteten og returnerer til forrige skjerm
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}