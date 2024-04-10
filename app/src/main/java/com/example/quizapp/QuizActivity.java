package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.viewmodel.ImageViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private ImageView quizImageView;
    private TextView textViewQuiz;
    private Button answerButton1, answerButton2, answerButton3;
    private ImageViewModel imageViewModel;
    private List<ImageEntity> shownImages = new ArrayList<>();
    private String correctAnswer;
    private int score = 0;
    private int attempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizImageView = findViewById(R.id.imageView);
        textViewQuiz = findViewById(R.id.textView_Quiz);
        answerButton1 = findViewById(R.id.btn_1);
        answerButton2 = findViewById(R.id.btn_2);
        answerButton3 = findViewById(R.id.btn_3);

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        loadImagesAndSetupQuiz();
    }

    private void loadImagesAndSetupQuiz() {
        imageViewModel.getAllImages().observe(this, imageEntities -> {
            // Filter out shown images
            List<ImageEntity> unusedImages = new ArrayList<>();
            for (ImageEntity image : imageEntities) {
                if (!shownImages.contains(image)) {
                    unusedImages.add(image);
                }
            }

            if (unusedImages.isEmpty()) {
                textViewQuiz.setText("Quiz over! No more images left.");
                new Handler().postDelayed(this::finish, 2000);
                return;
            }

            setupQuiz(unusedImages);
        });
    }

    private void setupQuiz(List<ImageEntity> unusedImages) {
        Collections.shuffle(unusedImages);
        ImageEntity correctImage = unusedImages.get(0);

        shownImages.add(correctImage);
        correctAnswer = correctImage.getImageName();

        quizImageView.setImageURI(Uri.parse(correctImage.getImagePath().toString()));

        // Prepare answer options
        List<String> answers = new ArrayList<>();
        answers.add(correctAnswer); // Correct answer
        // Add wrong answers
        while (answers.size() < 3) {
            ImageEntity randomImage = unusedImages.get(new Random().nextInt(unusedImages.size()));
            if (!answers.contains(randomImage.getImageName())) {
                answers.add(randomImage.getImageName());
            }
        }
        Collections.shuffle(answers);

        answerButton1.setText(answers.get(0));
        answerButton2.setText(answers.get(1));
        answerButton3.setText(answers.get(2));

        // Setting click listeners
        answerButton1.setOnClickListener(v -> checkAnswer(answers.get(0)));
        answerButton2.setOnClickListener(v -> checkAnswer(answers.get(1)));
        answerButton3.setOnClickListener(v -> checkAnswer(answers.get(2)));
    }

    private void checkAnswer(String selectedAnswer) {
        attempts++;
        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            Toast.makeText(this, "Riktig!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Feil, prøv igjen.", Toast.LENGTH_SHORT).show();
        }
        textViewQuiz.setText("Score: " + score + "   Attempts: " + attempts);
        // Delay before next question
        new Handler().postDelayed(this::loadImagesAndSetupQuiz, 2000);
    }
}