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

import com.example.quizapp.R;
import com.example.quizapp.adapter.RecyclerViewAdapter;
import com.example.quizapp.adapter.RecyclerViewInterface;
import com.example.quizapp.database.AppDatabase;
import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private static final int GALLERY_REQUEST = 1; // Class constant for gallery request
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ImageViewModel imageViewModel;
    private boolean sorted = true;
    private RecyclerViewAdapter recyclerViewAdapter;

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
            // Oppdaterer RecyclerViewAdapter med den nye listen av bilder
            recyclerViewAdapter.setImages(images);
        });
    }

    /**
     * @param view The view that triggered this method, typically a sort button in the user interface.
     */
    public void sortButton(View view){
        // Refresh the RecyclerView
        refreshRecyclerView();

        Button sortButton = (Button) findViewById(R.id.sort_btn);
        // Må fikse her TODO
        if (true) {
            sortButton.setText("Sort A-Z");
        } else {
            sortButton.setText("Sort Z-A");
        }
    }

    public void addButton(View view){
        // Launch the gallery to pick an image
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    /**
     * Handles the result from launching the gallery for image selection.
     * If the result is OK and the request code matches, prompts the user to enter a name for the new image.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing identification of who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached as Extras).
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            Uri imageUri = data.getData();
            // Prompt for entering a name
            promptForImageName(imageUri);
        }
    }

    /**
     * Displays a dialog prompting the user to enter a name for the new image selected from the gallery.
     * If a name is entered and confirmed with "OK", a new ImageEntity instance is created and added to the database.
     * The RecyclerView is then refreshed to include the newly added animal.
     *
     * @param imageUri The URI of the selected image to be associated with the new image.
     */
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
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
        }
    }

    @Override
    public void onItemClick(int position) {
        // Hent informasjon om bildet basert på posisjon - dette krever endringer i ViewModel
        // For demonstrasjonsformål antar vi at ImageViewModel har en metode for å hente ImageEntity basert på ID/posisjon
        //    imageViewModel.getImageById(position).observe(this, imageEntity -> {
        //        if (imageEntity != null) {
        //            // Starter DeleteImageActivity med bildeinformasjon
        //            Intent intent = new Intent(this, DeleteImageActivity.class);
        //            intent.putExtra("NAME", imageEntity.getImageName());
        //            Uri imageUri = Uri.parse(imageEntity.getImagePath());
        //            intent.putExtra("IMAGE", imageUri.toString());
        //            startActivity(intent);
        //        }
        //    });
    }
}