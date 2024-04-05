package com.example.quizapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewImageActivity extends AppCompatActivity {


    private ImageView imageView;
    // Registers a photo picker activity launcher
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::handleResult);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_image);

        imageView = findViewById(R.id.chosenImageView);

        Button chooseImageButton = findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(view -> launchPhotoPicker());
    }

    private void launchPhotoPicker() {
        // Launch the photo picker for images
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void handleResult(@Nullable Uri uri) {
        // Callback is invoked after the user selects a media item or closes the photo picker
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: " + uri);
            imageView.setImageURI(uri); // Display the selected image in the ImageView
        } else {
            Log.d("PhotoPicker", "No media selected");
        }
    }
}