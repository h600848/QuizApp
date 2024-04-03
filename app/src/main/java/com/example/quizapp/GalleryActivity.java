package com.example.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.adapter.RecyclerViewAdapter;
import com.example.quizapp.adapter.RecyclerViewInterface;
import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

import java.util.Collections;
import java.util.Comparator;

public abstract class GalleryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ImageViewModel imageViewModel;
    private boolean sorted = true;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Button sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        setupActivityResultLauncher();
        setupView();
        setupButtons();
        observerSetup();
    }

    private void observerSetup() {
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
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    private void setupActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        handleActivityResult(result.getData());

                    }
                });
    }

    private void handleActivityResult(Intent data) {
        if (data != null) {
            Uri uri = Uri.parse(data.getStringExtra("imageUri"));
            int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            getContentResolver().takePersistableUriPermission(uri, flag);
            String name = data.getStringExtra("inputText");
            recyclerViewAdapter.setImages(Collections.singletonList(new ImageEntity(name, uri)));
        }
    }

    private void setupButtons() {
        Button add = findViewById(R.id.add_btn);
        sort = findViewById(R.id.sort_btn);

        add.setOnClickListener(v -> launchImageActivity());

        sort.setOnClickListener(v -> sortlogic());
    }

    private void sortlogic() {
        imageViewModel.getAllImages().observe(this, imageList -> {

            if (!imageList.isEmpty()) {
                //sort a to z
                if(sorted) {
                    imageList.sort(Comparator.comparing(ImageEntity::getImageName));
                    sort.setText("Sort Z-A");
                } else {
                    imageList.sort(Comparator.comparing(ImageEntity::getImageName).reversed());
                    sort.setText("Sort A-Z");
                }
            }
            sorted = !sorted;
            recyclerView.setAdapter(recyclerViewAdapter);
        });
    }

    private void launchImageActivity() {
        Intent intent = new Intent(GalleryActivity.this, DeleteImageActivity.class);
        activityResultLauncher.launch(intent);
    }
}
