package com.example.quizapp;

import java.io.Serializable;

public class GalleryItem implements Serializable {
    private String name;
    private int imageResId = -1; // For lokale drawables, -1 indikerer at dette feltet ikke er i bruk
    private String imagePath = null; // For bilder fra URI

    // Konstruktør for drawable ressurser
    public GalleryItem(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    // Konstruktør for bilde-URI
    public GalleryItem(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getImagePath() {
        return imagePath;
    }

    // Hjelpemetode for å sjekke om bildet er en drawable ressurs
    public boolean isDrawableResource() {
        return imageResId != -1;
    }
}