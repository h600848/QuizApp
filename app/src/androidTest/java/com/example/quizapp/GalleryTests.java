package com.example.quizapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testklasse for GalleryActivity som bruker AndroidJUnit4 testkjører.
 * Denne klassen utfører automatiserte UI-tester for å verifisere at galleriets funksjonalitet,
 * som å legge til, vise og slette bilder, fungerer som forventet.
 * Bruker Espresso for å simulere brukerinteraksjoner og sjekke UI-responser.
 */
@RunWith(AndroidJUnit4.class)
public class GalleryTests {

    /**
     * Setter opp en ActivityScenarioRule for GalleryActivity for å sikre
     * at aktiviteten er startet før hver test. Denne regelen håndterer også
     * oppryddingsprosessen etter hver testutførelse, og gir en fresh start.
     */
    @Rule
    public ActivityScenarioRule<GalleryActivity> activityScenarioRule
            = new ActivityScenarioRule<>(GalleryActivity.class);

    /**
     * Tester funksjonaliteten for å legge til et nytt bilde i galleriet.
     * Den sjekker følgende sekvens av handlinger:
     * 1. Initialisering av Intents for å spore levetiden og handlingene til intents.
     * 2. Verifiserer starttilstanden til RecyclerView for å inneholde et spesifikt antall objekter.
     * 3. Lanserer AddNewImageActivity og simulerer bildeseleksjon.
     * 4. Sjekker synligheten av det valgte bildet i brukergrensesnittet.
     * 5. Simulerer lagring av bildet og sjekker om RecyclerView oppdateres korrekt.
     * 6. Rydder opp Intents etter testen.
     */
    @Test
    public void testAddImage(){

        Uri imageUri = Uri.parse("android.resource://com.example.quizapp/" + R.drawable.sample);

        Intents.init();
        // Initialsjekk for antall elementer i RecyclerView
        onView(withId(R.id.myRecyclerView)).check(matches(hasChildCount(3)));


        // Starter AddNewImageActivity og simulerer valg av bilde
        ActivityScenario<AddNewImageActivity> scenario = ActivityScenario.launch(AddNewImageActivity.class);
        //Intents.inteded er en funksjon som brukes til å verifisere at en spesefikk intent har blitt sendt
        Intents.intended(hasComponent(AddNewImageActivity.class.getName()));
        Intent imageTest = new Intent();
        imageTest.setData(imageUri);
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, imageTest);

        // Stubber intenten som håndterer bildevalg
        // Den kaller ACTION_OPEN_DOCUMENT men gir inn ActivityResult som svar
        // Som vil si at den får en RESULT_OK og imageTest Intenten som et resultat
        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result);

        // Sjekker at bildet vises i bildevisningen
        onView(withId(R.id.chosenImageView)).check(matches(isDisplayed()));

        // Simulerer inntasting av bilde navn og lagring
        onView(withId(R.id.imageNameInput)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        // Sjekker at galleriet oppdateres korrekt
        ActivityScenario<GalleryActivity> galleryScenario = ActivityScenario.launch(GalleryActivity.class);
        Intents.intended(hasComponent(GalleryActivity.class.getName()));
        onView(withId(R.id.myRecyclerView)).check(matches(hasChildCount(4)));
        Intents.release();
    }

    /**
     * Verifiserer at 'legg til'-knappen i GalleryActivity starter AddNewImageActivity.
     * Denne testen sjekker:
     * 1. Synligheten og klikkbarheten til 'legg til'-knappen.
     * 2. At riktig aktivitet startes ved å klikke på 'legg til'-knappen.
     */
    @Test
    public void testAddButtonGoesToCorrectActivity(){
        Intents.init();
        //Tests the add Button in Gallery
        onView(withId(R.id.add_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.add_btn)).check(matches(isClickable()));
        onView(withId(R.id.add_btn)).perform(click());

        //Checks if the correct Activity is displayed
        Intents.intended(hasComponent(AddNewImageActivity.class.getName()));

        Intents.release();
    }


}