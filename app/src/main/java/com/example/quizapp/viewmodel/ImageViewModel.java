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

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository repository;
    private LiveData<List<ImageEntity>> allImages;
    private int score = 0;
    private int attempts = 0;
    // Ny variabel for å lagre URI-en til det nåværende bildet
    private MutableLiveData<Uri> currentImageUri = new MutableLiveData<>();

    public ImageViewModel(@NonNull Application application) {
        super(application);
        repository = new ImageRepository(application);
        allImages = repository.getAllImages();
    }

    public LiveData<List<ImageEntity>> getAllImages() {
        return allImages;
    }

    public ImageEntity selectRandomImage(List<ImageEntity> images) {
        return images.get(new Random().nextInt(images.size()));
    }

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

    // Metode for å sette inn et bilde
    public void insertImage(ImageEntity imageEntity) {
        repository.insertImage(imageEntity);
    }

    // Metode for å slette et bilde med ID
    public void deleteImageById(int id) {
        repository.deleteImageById(id);
    }

    // Metode for å hente et bilde med ID
    public LiveData<ImageEntity> getImageById(int id) {
        return repository.getImageById(id);
    }

    // Getter for bildets URI
    public LiveData<Uri> getCurrentImageUri() {
        return currentImageUri;
    }

    // Setter for bildets URI
    public void setCurrentImageUri(Uri uri) {
        currentImageUri.setValue(uri);
    }

    public String getCurrentImageUriAsString() {
        Uri currentUri = currentImageUri.getValue();
        return (currentUri != null) ? currentUri.toString() : null;
    }
}