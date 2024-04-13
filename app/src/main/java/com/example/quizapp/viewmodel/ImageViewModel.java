package com.example.quizapp.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quizapp.model.ImageEntity;
import com.example.quizapp.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Definerer ImageViewModel-klassen som håndterer UI-relatert data.
public class ImageViewModel extends AndroidViewModel {
    private ImageRepository repository;
    private LiveData<List<ImageEntity>> allImages;
    private int score = 0;
    private int attempts = 0;

    // Ny variabel for å lagre URI-en til det nåværende bildet
    private MutableLiveData<Uri> currentImageUri = new MutableLiveData<>();

    // Konstruktør som initialiserer repository og henter alle bilder.
    public ImageViewModel(@NonNull Application application) {
        super(application);
        repository = new ImageRepository(application);
        allImages = repository.getAllImages();
    }

    // Returnerer LiveData med alle bilder.
    public LiveData<List<ImageEntity>> getAllImages() {
        return allImages;
    }

    // Velger et tilfeldig bilde fra listen for quiz.
    public ImageEntity selectRandomImage(List<ImageEntity> images) {
        return images.get(new Random().nextInt(images.size()));
    }

    // Forbereder et sett med mulige svar for quiz.
    public List<String> prepareAnswers(List<ImageEntity> images, ImageEntity correctImage) {
        List<String> answers = new ArrayList<>();
        answers.add(correctImage.getImageName());
        while (answers.size() < 3) {
            ImageEntity randomImage = images.get(new Random().nextInt(images.size()));
            if (!answers.contains(randomImage.getImageName())) {
                answers.add(randomImage.getImageName());
            }
        }
        return answers;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        attempts++;
    }

    // Setter inn et bilde i databasen.
    public void insertImage(ImageEntity imageEntity) {
        repository.insertImage(imageEntity);
    }

    // Sletter et bilde med ID.
    public void deleteImageById(int id) {
        repository.deleteImageById(id);
    }

    // Henter et bilde med ID.
    public LiveData<ImageEntity> getImageById(int id) {
        return repository.getImageById(id);
    }

    // Returnerer LiveData for bildets URI.
    public LiveData<Uri> getCurrentImageUri() {
        return currentImageUri;
    }

    // Setter URI for det nåværende bildet.
    public void setCurrentImageUri(Uri uri) {
        currentImageUri.setValue(uri);
    }

    // Returnerer bildets URI som en streng.
    public String getCurrentImageUriAsString() {
        Uri currentUri = currentImageUri.getValue();
        return (currentUri != null) ? currentUri.toString() : null;
    }
}