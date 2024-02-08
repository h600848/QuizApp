package com.example.quizapp;

import java.util.ArrayList;

public class GalleryDataManager {
    private static final GalleryDataManager ourInstance = new GalleryDataManager();

    public static GalleryDataManager getInstance() {
        return ourInstance;
    }

    private ArrayList<GalleryItem> galleryItems = new ArrayList<>();

    private GalleryDataManager() {
    }

    public ArrayList<GalleryItem> getGalleryItems() {
        return galleryItems;
    }

    public void setGalleryItems(ArrayList<GalleryItem> galleryItems) {
        this.galleryItems = galleryItems;
    }

}
