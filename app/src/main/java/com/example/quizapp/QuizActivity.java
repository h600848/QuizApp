package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
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

        // Hardkoder items for testing, det funket
        // items = new ArrayList<>();
        // items.add(new GalleryItem("Gorilla", R.drawable.gorilla));
        // items.add(new GalleryItem("Isbjørn", R.drawable.isbjorn));
        // items.add(new GalleryItem("Hai", R.drawable.hai));

        // Forsøker å motta GalleryItem-listen som er sendt fra MainActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("galleryItems")) {
            items = (ArrayList<GalleryItem>) intent.getSerializableExtra("galleryItems");
        } else {
            // Hvis ingen data er mottatt, initialiserer items til en tom liste
            items = new ArrayList<>();
        }

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

        // Henter referanser til bildene(UI-komponenter)
        quizImageView = findViewById(R.id.quizImageView);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);

        // Viser bildet for valgt GalleryItem
        quizImageView.setImageResource(currentItem.getImageId());

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
        if(selectedAnswer.equals(correctAnswer)) {
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
        }, 3500); // Venter 5000 millisekunder (3,5 sekunder) før neste spørsmål settes opp
    }
}