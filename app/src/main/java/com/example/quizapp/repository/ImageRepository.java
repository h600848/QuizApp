package com.example.quizapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.quizapp.database.AppDatabase;
import com.example.quizapp.database.ImageDAO;
import com.example.quizapp.model.ImageEntity;

import java.util.List;

public class ImageRepository {
    private static ImageDAO imageDao;
    private LiveData<List<ImageEntity>> allImages;

    public ImageRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        imageDao = db.imageDao();
        allImages = imageDao.getAllImages();
    }

    public LiveData<List<ImageEntity>> getAllImages() {
        return allImages;
    }

    private static class InsertAsyncTask extends AsyncTask<ImageEntity, Void, Void> {
        private final ImageDAO asyncTaskDao;

        InsertAsyncTask(ImageDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ImageEntity... imageEntity) {
            asyncTaskDao.insertImage(imageEntity[0]);
            return null;
        }
    }

    public void insertImage(ImageEntity imageEntity) {
        InsertAsyncTask task = new InsertAsyncTask(imageDao);
        task.execute(imageEntity);
    }

    public void deleteImage(ImageEntity imageEntity) {
        new Thread(() -> imageDao.delete(imageEntity)).start();
    }

    public void deleteAll() {
        new Thread(() -> imageDao.deleteAll()).start();
    }

    public LiveData<ImageEntity> getImageById(int id) {
        return imageDao.getImageById(id);
    }

    public void deleteImageById(int id) {
        new Thread(() -> imageDao.deleteImageById(id)).start();
    }
}