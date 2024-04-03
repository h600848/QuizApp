package com.example.quizapp.database;

import android.net.Uri;

import androidx.room.TypeConverter;

public class Converter {

    @TypeConverter
    public static String UriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    @TypeConverter
    public static Uri StringToUri (String string) {
        return string == null ? null : Uri.parse(string);
    }
}