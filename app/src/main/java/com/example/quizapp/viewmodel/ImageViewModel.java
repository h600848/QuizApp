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

/**
 * ViewModel-klasse for å håndtere UI-relatert data og forretningslogikk knyttet til bildevisning og interaksjon.
 * Denne klassen fungerer som broen mellom UI-komponentene og underliggende data modeller, håndterer datamanipulasjon,
 * og sørger for at UI-komponentene mottar oppdaterte data.
 */
public class ImageViewModel extends AndroidViewModel {
    private ImageRepository repository; // Repository som håndterer databasetilgang
    private LiveData<List<ImageEntity>> allImages; // LiveData som holder alle bildene fra databasen
    private int score = 0;
    private int attempts = 0;

    // LiveData for å holde styr på den nåværende bildets URI som vises i UI
    private MutableLiveData<Uri> currentImageUri = new MutableLiveData<>();

    /**
     * Konstruktør som tar en applikasjon som parameter og initialiserer ImageRepository samt laster inn alle bildene.
     *
     * @param application Gir kontekst og tilgang til applikasjonens livssyklus.
     */
    public ImageViewModel(@NonNull Application application) {
        super(application);
        repository = new ImageRepository(application);
        allImages = repository.getAllImages();
    }

    /**
     * Returnerer en LiveData som inneholder en liste over alle bildene, slik at UI kan observere og reagere på endringer.
     *
     * @return LiveData med liste over ImageEntity.
     */
    public LiveData<List<ImageEntity>> getAllImages() {
        return allImages;
    }

    /**
     * Velger et tilfeldig bilde fra listen, brukt for å generere quizspørsmål.
     *
     * @param images Liste over ImageEntity hvor et tilfeldig bilde vil bli valgt.
     * @return Et tilfeldig valgt ImageEntity.
     */
    public ImageEntity selectRandomImage(List<ImageEntity> images) {
        return images.get(new Random().nextInt(images.size()));
    }

    /**
     * Forbereder et sett med svaralternativer for en quiz, inkludert det korrekte svaret og tilfeldige feil svar.
     *
     * @param images Liste over tilgjengelige bilder.
     * @param correctImage Det korrekte bildet som svaret skal inkludere.
     * @return En liste med svaralternativer.
     */
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

    /**
     * Setter inn et nytt bilde i databasen gjennom repository.
     *
     * @param imageEntity Bildeentiteten som skal lagres.
     */
    public void insertImage(ImageEntity imageEntity) {
        repository.insertImage(imageEntity);
    }

    /**
     * Sletter et bilde basert på ID fra databasen.
     *
     * @param id ID-en til bildet som skal slettes.
     */
    public void deleteImageById(int id) {
        repository.deleteImageById(id);
    }

    /**
     * Henter et spesifikt bilde basert på ID, returnerer LiveData som kan observeres av UI.
     *
     * @param id ID-en til bildet som skal hentes.
     * @return LiveData som representerer det spesifikke bildet.
     */
    public LiveData<ImageEntity> getImageById(int id) {
        return repository.getImageById(id);
    }

    /**
     * Returnerer LiveData som inneholder URI for det nåværende bildet.
     *
     * @return LiveData med URI for det nåværende bildet.
     */
    public LiveData<Uri> getCurrentImageUri() {
        return currentImageUri;
    }

    /**
     * Setter URI-en for det nåværende bildet og oppdaterer den observerbare LiveData.
     *
     * @param uri URI-en til det nye bildet som skal vises.
     */
    public void setCurrentImageUri(Uri uri) {
        currentImageUri.setValue(uri);
    }

    /**
     * Konverterer URI-en til det nåværende bildet til en streng, primært for logging eller direkte visning i UI.
     *
     * @return Strengrepresentasjon av bildets URI.
     */
    public String getCurrentImageUriAsString() {
        Uri currentUri = currentImageUri.getValue();
        return (currentUri != null) ? currentUri.toString() : null;
    }
}
