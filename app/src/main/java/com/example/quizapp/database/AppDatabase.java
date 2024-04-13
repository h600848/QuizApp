package com.example.quizapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.quizapp.model.ImageEntity;

/**
 * Definerer en Room Database med en tabell basert på ImageEntity-klassen.
 * Denne klassen håndterer databaseversjonering og konvertering av spesialiserte datatyper via TypeConverters.
 */
@Database(entities = {ImageEntity.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Abstrakt metode som gir tilgang til databasens DAO (Data Access Object) for bildebehandling.
     * DAO er et mønster som gir en abstrakt grensesnitt til en database. Ved å definere DAO som en abstrakt metode,
     * lar vi Room biblioteket automatisk implementere nødvendige detaljer basert på annotasjoner i ImageDAO-interfacet.
     */
    public abstract ImageDAO imageDao();
    // En 'static' referanse sikrer at det kun eksisterer én instans av databasen gjennom applikasjonens levetid.
    private static AppDatabase INSTANCE;


    /**
     * Singleton-metode som sikrer at det kun finnes en enkelt instans av AppDatabase globalt.
     * Bruk av 'synchronized' blokk forhindrer samtidig tilgang og instansiering fra flere tråder,
     * noe som er essensielt for å unngå flere databaser som åpnes samtidig.
     *
     * @param context Applikasjonens kontekst, brukes for å tilpasse databasekonfigurasjonen til applikasjonens miljø.
     * @return En singleton-instans av AppDatabase.
     */
    public static AppDatabase getDatabase(final Context context) {
        // Dobbel-sjekk for å sikre at kun en instans av databasen blir opprettet.
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Oppretter databasen ved å bygge den med Room.databaseBuilder.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "image_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}