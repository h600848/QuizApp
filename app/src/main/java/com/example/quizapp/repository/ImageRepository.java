package com.example.quizapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.quizapp.database.AppDatabase;
import com.example.quizapp.database.ImageDAO;
import com.example.quizapp.model.ImageEntity;

import java.util.List;

// Definerer ImageRepository-klassen som håndterer dataoperasjoner.
public class ImageRepository {
    private static ImageDAO imageDao;
    private LiveData<List<ImageEntity>> allImages;

    // Konstruktør som initialiserer ImageDAO og henter alle bildene.
    public ImageRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        imageDao = db.imageDao();
        allImages = imageDao.getAllImages();
    }

    // Returnerer et LiveData-objekt som inneholder en liste over alle bilder.
    public LiveData<List<ImageEntity>> getAllImages() {
        return allImages;
    }

    // Asynkron oppgaveklasse for å utføre databasinnsetting uten å blokkere UI-tråden.
    private static class InsertAsyncTask extends AsyncTask<ImageEntity, Void, Void> {
        private final ImageDAO asyncTaskDao;

        // Konstruktør som tar en ImageDAO som parameter.
        InsertAsyncTask(ImageDAO dao) {
            asyncTaskDao = dao;
        }

        // Utfører databasoperasjonen i bakgrunnen.
        @Override
        protected Void doInBackground(ImageEntity... imageEntity) {
            asyncTaskDao.insertImage(imageEntity[0]);
            return null;
        }
    }

    // Metode for å sette inn et bilde via AsyncTask.
    public void insertImage(ImageEntity imageEntity) {
        InsertAsyncTask task = new InsertAsyncTask(imageDao);
        task.execute(imageEntity);
    }

    // Henter et bilde basert på ID.
    public LiveData<ImageEntity> getImageById(int id) {
        return imageDao.getImageById(id);
    }

    // Sletter et bilde asynkront ved å bruke en tråd.
    public void deleteImageById(int id) {
        new Thread(() -> imageDao.deleteImageById(id)).start();
    }
}