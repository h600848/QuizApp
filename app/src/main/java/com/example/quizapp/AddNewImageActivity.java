package com.example.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

/**
 * Aktivitet for å legge til nye bilder i applikasjonen. Denne klassen håndterer brukergrensesnittet for
 * å velge et bilde fra enhetens lagring og lagre det i applikasjonens database.
 */
public class AddNewImageActivity extends AppCompatActivity {
    private Uri imageUri;
    private ImageViewModel imageViewModel; // ViewModel for å håndtere datainteraksjoner
    private ImageView imageView; // Viser det valgte bildet


    // Registrerer en 'photo picker activity launcher' for å tillate bildevalg fra enhetens lagring
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::handleResult);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_image);

        // Initialiserer ViewModel
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        imageView = findViewById(R.id.chosenImageView);

        Button chooseImageButton = findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(view -> launchPhotoPicker());

        Button saveButton = findViewById(R.id.saveButton);

        TextView text = findViewById(R.id.imageNameInput);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewImage(text.getText().toString(), imageUri);
            }
        });
    }

    /**
     * Initierer fotovalgergrensesnittet slik at brukeren kan velge et bilde.
     */
    private void launchPhotoPicker() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    /**
     * Håndterer resultatet fra bildevelgeren. Hvis et bilde blir valgt, setter den bildets URI og oppdaterer ImageView.
     * @param uri URI-en til det valgte bildet.
     */
    private void handleResult(@Nullable Uri uri) {
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: " + uri);
            imageView.setImageURI(uri); // Oppdaterer ImageView med det valgte bildet
            imageUri = uri;
            // Gir en varig URI-tillatelse for å tilgang til bildet på tvers av omstarter
            int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            this.getContentResolver().takePersistableUriPermission(uri, flag);
        } else {
            Log.d("PhotoPicker", "No media selected");
        }
    }

    /**
     * Lagrer det nye bildet i databasen ved hjelp av det angitte navnet og URI-en.
     * @param name Navnet på det nye bildet, som brukeren har angitt.
     * @param uri URI-en til det nye bildet.
     */
    private void saveNewImage(String name, Uri uri) {
        ImageEntity image = new ImageEntity(name, uri);
        imageViewModel.insertImage(image);
        finish(); // Lukker denne aktiviteten og returnerer til forrige skjerm
    }
}