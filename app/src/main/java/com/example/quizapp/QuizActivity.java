package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private ImageView quizImageView;
    private TextView textViewQuiz;
    private Button answerButton1, answerButton2, answerButton3;
    private ImageViewModel imageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        bindViews();
        setupViewModel();

        // For rotasjon
        if (savedInstanceState != null) {
            textViewQuiz.setText(savedInstanceState.getString("currentQuizText", ""));

            // Gjenoppretter bildets URI fra savedInstanceState og oppdaterer ViewModel
            String savedImageUriAsString = savedInstanceState.getString("currentImageUri");
            if(savedImageUriAsString != null) {
                Uri savedImageUri = Uri.parse(savedImageUriAsString);
                imageViewModel.setCurrentImageUri(savedImageUri);
            }
        }
    }

    private void bindViews() {
        quizImageView = findViewById(R.id.imageView);
        textViewQuiz = findViewById(R.id.textView_Quiz);
        answerButton1 = findViewById(R.id.btn_1);
        answerButton2 = findViewById(R.id.btn_2);
        answerButton3 = findViewById(R.id.btn_3);
    }

    private void setupViewModel() {
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        imageViewModel.getAllImages().observe(this, this::prepareNextRound);
    }

    private void prepareNextRound(List<ImageEntity> imageEntities) {
        if (!imageEntities.isEmpty()) {
            ImageEntity currentImage = imageViewModel.selectRandomImage(imageEntities);
            // Konverterer bildets sti til en URI
            Uri currentUri = Uri.parse(currentImage.getImagePath().toString());
            // Oppdaterer ViewModel med den nåværende bildets URI
            imageViewModel.setCurrentImageUri(currentUri);
            // Setter bildet i ImageView basert på URI
            quizImageView.setImageURI(currentUri);

            List<String> answers = imageViewModel.prepareAnswers(imageEntities, currentImage);
            Collections.shuffle(answers);

            answerButton1.setText(answers.get(0));
            answerButton2.setText(answers.get(1));
            answerButton3.setText(answers.size() > 2 ? answers.get(2) : "N/A");

            answerButton1.setOnClickListener(v -> checkAnswer(answers.get(0), currentImage.getImageName()));
            answerButton2.setOnClickListener(v -> checkAnswer(answers.get(1), currentImage.getImageName()));
            answerButton3.setOnClickListener(v -> checkAnswer(answers.size() > 2 ? answers.get(2) : "", currentImage.getImageName()));
        }
    }

    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        if (selectedAnswer.equals(correctAnswer)) {
            imageViewModel.incrementScore();
            imageViewModel.incrementAttempts();
            textViewQuiz.setText("Correct! Score: " + imageViewModel.getScore() + "   Attempts: " + imageViewModel.getAttempts());
        } else {
            imageViewModel.incrementAttempts();
            textViewQuiz.setText("Wrong! Score: " + imageViewModel.getScore() + "   Attempts: " + imageViewModel.getAttempts());
        }
        setupViewModel(); // Oppdater viewmodellen for neste runde
    }

    // For rotasjon
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentQuizText", textViewQuiz.getText().toString());
        // Henter den nåværende bildets URI som en streng fra ViewModel og lagrer den
        String imageUriAsString = imageViewModel.getCurrentImageUriAsString();
        if(imageUriAsString != null) {
            outState.putString("currentImageUri", imageUriAsString);
        }
    }
}