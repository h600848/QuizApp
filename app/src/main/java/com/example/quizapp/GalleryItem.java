package com.example.quizapp;

import java.io.Serializable;

public class GalleryItem implements Serializable {
    private String name; // Navn på bilde
    private int imageId; // Referanse til bildet

    public GalleryItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}
