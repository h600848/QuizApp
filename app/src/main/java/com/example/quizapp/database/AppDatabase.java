package com.example.quizapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.quizapp.model.ImageEntity;

// Deklarerer klassen som en Room-database med en spesifikk tabell for ImageEntity-objekter og en databaseversjon.
@Database(entities = {ImageEntity.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    // Abstrakt metode som skal gi tilgang til ImageDAO-implementasjonen.
    public abstract ImageDAO imageDao();
    private static AppDatabase INSTANCE;

    // Singleton-metode for å sikre at kun én instans av databasen eksisterer.
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Oppretter og returnerer en ny instans av databasen.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "image_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}