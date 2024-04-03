package com.example.quizapp.model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_table")
public class ImageEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int imageId; // ID-en genereres automatisk

    private final String imageName;

    private final Uri imagePath;

    public ImageEntity(String imageName, Uri imagePath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public Uri getImagePath() {
        return imagePath;
    }
}