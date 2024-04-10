package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizapp.viewmodel.ImageViewModel;

public class MainActivity extends AppCompatActivity {

    private ImageViewModel imageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
    }

    public void galleryButton(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    public void playButton(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }
}