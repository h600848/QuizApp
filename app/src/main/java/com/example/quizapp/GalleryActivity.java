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

public class GalleryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private static final int GALLERY_REQUEST = 1; // Class constant for gallery request
    private ImageViewModel imageViewModel;
    private boolean sorted = true;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        setupView();
        observeSetup();
    }

    private void observeSetup() {
        imageViewModel.getAllImages().observe(this, imageEntities -> {
            if (imageEntities.isEmpty()) {
                imageViewModel.insertImage(new ImageEntity("Gorilla", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.gorilla)));
                imageViewModel.insertImage(new ImageEntity("Polar bear", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.isbjorn)));
                imageViewModel.insertImage(new ImageEntity("Fox", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.fox)));
            }

            recyclerViewAdapter = new RecyclerViewAdapter(this, imageEntities, this);
            recyclerViewAdapter.setImages(imageEntities);
            recyclerView.setAdapter(recyclerViewAdapter);
        });
    }

    private void setupView() {
        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void sortButton(View view){
        // Refresh the RecyclerView
        refreshRecyclerView();

        Button sortButton = (Button) findViewById(R.id.sort_btn);

        imageViewModel.getAllImages().observe(this, imageList -> {

            if (!imageList.isEmpty()) {
                // Sorterer A til Z
                if(sorted) {
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

    private void refreshRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
        }
    }

    public void addButton(View view){
        // Launch the gallery to pick an image
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onItemClick(int position) {
            imageViewModel.getImageById(position).observe(this, imageEntity -> {
                if (imageEntity != null) {
                    // Starter DeleteImageActivity med bildeinformasjon
                    Intent intent = new Intent(this, DeleteImageActivity.class);
                    intent.putExtra("NAME", imageEntity.getImageName());
                    Uri imageUri = Uri.parse(imageEntity.getImagePath().toString());
                    intent.putExtra("IMAGE", imageUri.toString());
                    startActivity(intent);
                }
            });
    }
}
