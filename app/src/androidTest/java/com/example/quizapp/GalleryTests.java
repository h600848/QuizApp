package com.example.quizapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.net.Uri;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GalleryTests {
    @Rule
    public ActivityScenarioRule<GalleryActivity> activityScenarioRule
            = new ActivityScenarioRule<>(GalleryActivity.class);



    @Test
    public void testAddImage(){

        Uri imageUri = Uri.parse("res:///" + R.drawable.sample);

        Intents.init();
        //Tests the add Button in Gallery
        onView(withId(R.id.myRecyclerView)).check(matches(hasMinimumChildCount(3)));
        onView(withId(R.id.add_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.add_btn)).check(matches(isClickable()));
        onView(withId(R.id.add_btn)).perform(click());

        //Checks if the correct Activity is displayed
        Intents.intended(hasComponent(AddNewImageActivity.class.getName()));
        Intent imageTest = new Intent();
        imageTest.setData(imageUri);
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, imageTest);

        // Stub the PhotoPicker intent to return the mock image
        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result);

        onView(withId(R.id.chosenImageView)).check(matches(isDisplayed()));
        // Perform the action that opens the PhotoPicker
        //onView(withId(R.id.chooseImageButton)).perform(click());

        onView(withId(R.id.imageNameInput)).perform(typeText("Test"), closeSoftKeyboard());

        onView(withId(R.id.saveButton)).perform(click());
        Intents.intended(hasComponent(GalleryActivity.class.getName()));
        onView(withId(R.id.myRecyclerView)).check(matches(hasMinimumChildCount(4)));
    }

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

    //@Test
    public void addNewImageActivityTest(){
        Uri imageUri = Uri.parse("res:///" + R.drawable.sample);
        onView(withId(R.id.imageNameInput)).perform(typeText("Test"), closeSoftKeyboard());
    }
}