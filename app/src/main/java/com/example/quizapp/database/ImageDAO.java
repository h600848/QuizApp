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
    void insertImage(ImageEntity image);

    @Query("SELECT * FROM image_table WHERE imageId = :id")
    List<ImageEntity> findImageById(int id);

    @Delete
    void delete (ImageEntity image);

    @Query("SELECT * FROM image_table")
    LiveData<List<ImageEntity>> getAllImages();
}