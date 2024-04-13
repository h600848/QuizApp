package com.example.quizapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.quizapp.database.AppDatabase;
import com.example.quizapp.database.ImageDAO;
import com.example.quizapp.model.ImageEntity;

import java.util.List;

/**
 * Repository-klassen som fungerer som et mellomledd mellom databasen og UI,
 * og håndterer datatilgangsoperasjoner for ImageEntity-objekter.
 */
public class ImageRepository {
    // Statisk variabel for ImageDAO for å gjøre databasoperasjoner.
    private static ImageDAO imageDao;
    // Holder på en LiveData-liste av ImageEntity for observerbar datahåndtering.
    private LiveData<List<ImageEntity>> allImages;

    /**
     * Konstruktør som henter en instans av databasen og initialiserer DAO og LiveData-variabelen.
     *
     * @param application Gir kontekst nødvendig for å få tilgang til databasen.
     */
    public ImageRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        imageDao = db.imageDao();
        allImages = imageDao.getAllImages();
    }

    /**
     * Gir tilgang til alle bilder som LiveData, slik at UI kan observere endringer.
     *
     * @return LiveData-objekt som inneholder en liste over alle bilde-entiteter.
     */
    public LiveData<List<ImageEntity>> getAllImages() {
        return allImages;
    }

    /**
     * Asynkron oppgaveklasse for å utføre databasinnsetting uten å blokkere hovedtråden (UI-tråden).
     * Dette sikrer at applikasjonens grensesnitt forblir responsivt.
     *  AsyncTask er en generisk klasse der du kan spesifisere følgende typer:
     *    - Input: Type objekter som sendes til oppgaven ved utførelse.
     *    - Progress: Type objekter som brukes til å rapportere fremdrift.
     *    - Result: Type objekt som returneres etter utførelse.
     *  I dette tilfellet:
     *    - ImageEntity er input-typen, siden det er det objektet som vi vil sette inn i databasen.
     *    - Void brukes for fremdrift, da denne spesifikke oppgaven ikke rapporterer om fremdrift.
     *    - Void brukes også som resultat, siden det ikke er noe resultat som skal returneres etter innsettingen.
     */
    private static class InsertAsyncTask extends AsyncTask<ImageEntity, Void, Void> {
        private final ImageDAO asyncTaskDao;

        /**
         * Konstruktør som initialiserer DAO som brukes i asynkron oppgave.
         *
         * @param dao DAO-objekt som brukes til databasoperasjoner.
         */
        InsertAsyncTask(ImageDAO dao) {
            asyncTaskDao = dao;
        }

        /**
         * Utfører innsettingsoperasjonen i bakgrunnen.
         *
         * @param imageEntity Variabel antall ImageEntity-objekter som skal settes inn i databasen.
         * @return null  Returtypen er Void, noe som indikerer at metoden ikke har noen nyttig returverdi.
         *               I konteksten av AsyncTask, betyr bruk av Void at det ikke forventes noe resultat
         *               fra denne bakgrunnsoppgaven som skal videreformidles til UI-tråden etter fullførelse.
         *               Det benyttes hovedsakelig i tilfeller hvor oppgaven utføres for sin sideeffekt (som databasewriting),
         *               og ikke for å returnere data.
         */
        @Override
        protected Void doInBackground(ImageEntity... imageEntity) {
            asyncTaskDao.insertImage(imageEntity[0]);
            return null;
        }
    }

    /**
     * Offentlig metode for å starte AsyncTask som setter inn et bilde i databasen.
     *
     * @param imageEntity ImageEntity-objektet som skal settes inn.
     */
    public void insertImage(ImageEntity imageEntity) {
        InsertAsyncTask task = new InsertAsyncTask(imageDao);
        task.execute(imageEntity);
    }

    /**
     * Henter et spesifikt bilde fra databasen ved hjelp av dets ID, og returnerer dette som LiveData.
     *
     * @param id ID-en til bildet som skal hentes.
     * @return LiveData-objekt som representerer det spesifikke bildet.
     */
    public LiveData<ImageEntity> getImageById(int id) {
        return imageDao.getImageById(id);
    }

    /**
     * Starter en ny tråd for å slette et bilde fra databasen basert på ID,
     * for å unngå å blokkere UI-tråden med databasoperasjoner.
     *
     * @param id ID-en til bildet som skal slettes.
     */
    public void deleteImageById(int id) {
        new Thread(() -> imageDao.deleteImageById(id)).start();
    }
}