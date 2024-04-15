package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

import java.util.Collections;
import java.util.List;

/**
 * QuizActivity er ansvarlig for å presentere et quiz-spill til brukeren, hvor brukeren må gjenkjenne bilder.
 * Den håndterer visning av quizspørsmål, opptak av svar og viser oppdaterte poengsummer og forsøk.
 */
public class QuizActivity extends AppCompatActivity {
    public ImageView quizImageView; // ImageView for å vise gjeldende quiz-bilde
    private TextView textViewQuiz; // TextView for å vise poengsum og antall forsøk
    public Button answerButton1, answerButton2, answerButton3;
    private ImageViewModel imageViewModel; // ViewModel som håndterer data for denne aktiviteten
    private ImageEntity currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        bindViews();
        setupViewModel();
        observeSetup();

        // Håndterer tilstandsendringer, som skjermrotasjon, ved å gjenopprette tilstanden
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

    /**
     * Binder visningskomponentene til deres respektive elementer i layouten.
     */
    private void bindViews() {
        quizImageView = findViewById(R.id.imageView);
        textViewQuiz = findViewById(R.id.textView_Quiz);
        answerButton1 = findViewById(R.id.btn_1);
        answerButton2 = findViewById(R.id.btn_2);
        answerButton3 = findViewById(R.id.btn_3);
    }

    /**
     * Setter opp ViewModel for å hente og observere bildeinformasjon.
     * Dette sikrer at brukergrensesnittet oppdateres automatisk når dataendringer forekommer.
     */
    private void setupViewModel() {
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        imageViewModel.getAllImages().observe(this, this::prepareNextRound);
    }

    /**
     * Forbereder hvert nytt spørsmål ved å velge et tilfeldig bilde og tilhørende svaralternativer.
     * Oppdaterer UI-komponentene med nytt bilde og alternativer.
     * @param imageEntities Liste av bildeentiteter brukt til å generere quiz-spørsmål.
     */
    private void prepareNextRound(List<ImageEntity> imageEntities) {
        if (!imageEntities.isEmpty()) {
            currentImage = imageViewModel.selectRandomImage(imageEntities);
            // Konverterer bildets sti til en URI
            Uri currentUri = Uri.parse(currentImage.getImagePath().toString());
            // Oppdaterer ViewModel med den nåværende bildets URI
            imageViewModel.setCurrentImageUri(currentUri);
            // Setter bildet i ImageView basert på URI
            quizImageView.setImageURI(currentUri);

            try{
                List<String> answers = imageViewModel.prepareAnswers(imageEntities, currentImage);
                Collections.shuffle(answers);

                answerButton1.setText(answers.get(0));
                answerButton2.setText(answers.get(1));
                answerButton3.setText(answers.size() > 2 ? answers.get(2) : "N/A");

                answerButton1.setOnClickListener(v -> checkAnswer(answers.get(0), currentImage.getImageName()));
                answerButton2.setOnClickListener(v -> checkAnswer(answers.get(1), currentImage.getImageName()));
                answerButton3.setOnClickListener(v -> checkAnswer(answers.size() > 2 ? answers.get(2) : "", currentImage.getImageName()));
            }catch (IllegalStateException e){
                // Viser en feilmelding til brukeren f.eks. via en Toast
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();


            }
        }
    }

    public String getCurrentImageName() {
        return currentImage.getImageName();
    }


    /**
     * Sjekker om det valgte svaret er korrekt, oppdaterer score og forsøk, og viser oppdatert informasjon i TextView.
     * @param selectedAnswer Det svaret brukeren valgte.
     * @param correctAnswer Det riktige svaret for gjeldende bilde.
     */
    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        if (selectedAnswer.equals(correctAnswer)) {
            imageViewModel.incrementScore();
            imageViewModel.incrementAttempts();
            textViewQuiz.setText("Correct! Score: " + imageViewModel.getScore() + "   Attempts: " + imageViewModel.getAttempts());
        } else {
            imageViewModel.incrementAttempts();
            textViewQuiz.setText("Wrong! Score: " + imageViewModel.getScore() + "   Attempts: " + imageViewModel.getAttempts());
        }
        setupViewModel(); // Resetter og oppdaterer viewmodellen for neste runde
    }

    /**
     * Lagrer nødvendig tilstandsinformasjon før aktiviteten blir ødelagt, f.eks. under rotasjon.
     * @param outState Bundle hvor tilstandsinformasjon lagres.
     */
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

    private void observeSetup() {
        imageViewModel.getAllImages().observe(this, imageEntities -> {
            if (imageEntities.isEmpty()) {
                // Legger til standardbilder om ingen bilder finnes fra før
                imageViewModel.insertImage(new ImageEntity("Gorilla", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.gorilla)));
                imageViewModel.insertImage(new ImageEntity("Polar bear", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.isbjorn)));
                imageViewModel.insertImage(new ImageEntity("Fox", Uri.parse("android.resource://com.example.quizapp/" + R.drawable.fox)));
            }
        });
    }
}