package com.example.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.adapter.RecyclerViewAdapter;
import com.example.quizapp.adapter.RecyclerViewInterface;
import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private static final int GALLERY_REQUEST = 1;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ImageViewModel imageViewModel;
    private boolean sorted = true;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<ImageEntity> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        recyclerViewAdapter = new RecyclerViewAdapter(this, new ArrayList<>(), this);

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageViewModel.getAllImages().observe(this, images -> {
            imagesList = images; // Oppdater lokal liste
            refreshRecyclerView();
        });
    }

    public void sortButton(View view) {
        Collections.sort(imagesList, (o1, o2) -> sorted ? o1.getImageName().compareTo(o2.getImageName()) : o2.getImageName().compareTo(o1.getImageName()));
        sorted = !sorted; // Endre sorteringstilstand

        Button sortButton = (Button) findViewById(R.id.sort_btn);
        sortButton.setText(sorted ? "Sort Z-A" : "Sort A-Z");

        recyclerViewAdapter.setImages(imagesList);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void addButton(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            Uri imageUri = data.getData();
            promptForImageName(imageUri);
        }
    }

    private void promptForImageName(Uri imageUri) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
                .setTitle("Add a name for the picture")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        ImageEntity newImage = new ImageEntity(name, imageUri);
                        imageViewModel.insertImage(newImage);
                        imagesList.add(newImage);
                        refreshRecyclerView();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshRecyclerView() {
        recyclerViewAdapter.setImages(imagesList);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        // Implementasjonen her avhenger av funksjonaliteten du ønsker å implementere ved klikk
    }
}