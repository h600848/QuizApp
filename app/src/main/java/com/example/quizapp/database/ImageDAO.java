package com.example.quizapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.quizapp.model.ImageEntity;

@Dao
public interface ImageDAO {
    @Insert
    void insertImage(ImageEntity imageEntity);

    @Query("SELECT * FROM image_table WHERE imageId = :id")
    LiveData<ImageEntity> getImageById(int id);

    @Delete
    void delete(ImageEntity imageEntity);

    @Query("SELECT * FROM image_table")
    LiveData<List<ImageEntity>> getAllImages();

    @Query("DELETE FROM image_table")
    void deleteAll();
}