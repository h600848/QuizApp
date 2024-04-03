package com.example.quizapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.quizapp.model.ImageEntity;

@Database(entities = {ImageEntity.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ImageDAO imageDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Oppretter databasen her
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "image_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}