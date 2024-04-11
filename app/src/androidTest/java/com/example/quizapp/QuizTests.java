package com.example.quizapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QuizTests {

    @Rule
    public ActivityScenarioRule<QuizActivity> activityScenarioRule
            = new ActivityScenarioRule<>(QuizActivity.class);

    @Test
    public void scoreTestV2(){
        onView(withId(R.id.btn_1)).perform(click());
        ViewInteraction scoreText = onView(withId(R.id.textView_Quiz));
        try {
            scoreText.check(matches(withSubstring("Correct!")));
        } catch (AssertionError e){
            scoreText.check(matches(withSubstring("Wrong!")));
        }
    }
}