package com.example.quizapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainMenuTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testPlayButton(){
        //Initialiserer Intents-testrammeverket for å fange opp alle intents som sendes.
        Intents.init();

        onView(withId(R.id.play_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.play_btn)).check(matches(isClickable()));
        onView(withId(R.id.play_btn)).perform(click());

        //Bekrefter at det er sendt en intent for å starte GalleryActivity
        Intents.intended(hasComponent(QuizActivity.class.getName()));
        //Renser opp Intents-rammeverket og stopper å fange opp intents.
        Intents.release();
    }

    @Test
    public void testGalleryBtn(){
        //Initialiserer Intents-testrammeverket for å fange opp alle intents som sendes.
        Intents.init();

        onView(withId(R.id.gallery_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.gallery_btn)).check(matches(isClickable()));
        onView(withId(R.id.gallery_btn)).perform(click());

        //Bekrefter at det er sendt en intent for å starte GalleryActivity
        Intents.intended(hasComponent(GalleryActivity.class.getName()));
        //Renser opp Intents-rammeverket og stopper å fange opp intents.
        Intents.release();
    }
}