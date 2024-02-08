package com.example.quizapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private List<GalleryItem> items;
    private ImageView quizImageView;
    private Button choice1, choice2, choice3;
    private String correctAnswer;
    private int score = 0, attempts = 0;
    private TextView feedbackTextView, scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Henter listen direkte fra GalleryDataManager
        items = GalleryDataManager.getInstance().getGalleryItems();

        // Initialiserer views
        feedbackTextView = findViewById(R.id.feedbackTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        quizImageView = findViewById(R.id.quizImageView);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);

        setUpQuiz();
    }

    private void setUpQuiz() {
        // Sjekker at items-listen ikke er tom
        if (items == null || items.isEmpty()) {
            return;
        }

        // Velger et tilfeldig GalleryItem
        int randomIndex = new Random().nextInt(items.size());
        GalleryItem currentItem = items.get(randomIndex);

        if (currentItem.isDrawableResource()) {
            quizImageView.setImageResource(currentItem.getImageResId());
        } else {
            Uri imageUri = Uri.parse(currentItem.getImagePath());
            // Bruker Glide til å laste bildet med en lytter for å fange opp suksess og feil
            Glide.with(this)
                    .load(imageUri)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Logger feil hvis bildet ikke kan lastes
                            Log.e("QuizActivity", "Failed to load image", e);
                            return false; // False indikerer at Glide ikke håndterer feilen selv
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Logger en melding når bildet er lastet suksessfullt
                            Log.d("QuizActivity", "Image loaded successfully");
                            return false; // False indikerer at Glide ikke håndterer ressursen selv
                        }
                    })
                    .into(quizImageView);
        }

        // Setter opp svaralternativer, et riktig og to feil
        List<String> choices = new ArrayList<>();
        choices.add(currentItem.getName()); // Legger til riktig svar
        correctAnswer = currentItem.getName(); // Lagrer det riktige svaret for senere sjekk

        // Genererer feil svar
        while (choices.size() < 3) {
            int wrongIndex = new Random().nextInt(items.size());
            String wrongAnswer = items.get(wrongIndex).getName();
            if (!choices.contains(wrongAnswer)) {
                choices.add(wrongAnswer);
            }
        }

        // Blander svarene for å vise dem i tilfeldig rekkefølge
        Collections.shuffle(choices);

        // Setter valgene til knappene
        choice1.setText(choices.get(0));
        choice2.setText(choices.get(1));
        choice3.setText(choices.get(2));

        // Setter på onClickListeners for å sjekke svaret
        View.OnClickListener answerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String selectedAnswer = clickedButton.getText().toString();
                checkAnswer(selectedAnswer);
            }
        };

        choice1.setOnClickListener(answerListener);
        choice2.setOnClickListener(answerListener);
        choice3.setOnClickListener(answerListener);
    }

    private void checkAnswer(String selectedAnswer) {
        attempts++;
        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            feedbackTextView.setText("Riktig!");
        } else {
            feedbackTextView.setText("Feil! Riktig svar er: " + correctAnswer);
        }
        scoreTextView.setText("Score: " + score + "/ Forsøk: " + attempts);

        // Forbereder neste spørsmål
        feedbackTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpQuiz();
            }
        }, 1500); // Venter 1500 millisekunder (1,5 sekunder) før neste spørsmål settes opp
    }
}