package com.example.quizapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.ImageView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QuizTests {
    String btnText = "";
    String imageViewText;

    @Rule
    public ActivityScenarioRule<QuizActivity> activityScenarioRule
            = new ActivityScenarioRule<>(QuizActivity.class);

    @Test
    public void scoreTestV2() throws InterruptedException {
        Thread.sleep(2000);
        ViewInteraction scoreText = onView(withId(R.id.textView_Quiz));

        activityScenarioRule.getScenario().onActivity(a -> {
            btnText = a.answerButton1.getText().toString();
            imageViewText = a.getCurrentImageName();
        });

        onView(withId(R.id.btn_1)).perform(click());

        if (btnText.equals(imageViewText)) {
            scoreText.check(matches(withSubstring("Correct! Score: ")));
        } else {
            scoreText.check(matches(withSubstring("Wrong! Score: ")));
        }
    }

    @Test
    public void scoreTestV3() throws InterruptedException {
        Thread.sleep(2000);
        ViewInteraction scoreText = onView(withId(R.id.textView_Quiz));

        activityScenarioRule.getScenario().onActivity(a -> {
            btnText = a.answerButton1.getText().toString();
            imageViewText = a.getCurrentImageName();
        });

        onView(withId(R.id.btn_1)).perform(click());

        if (btnText.equals(imageViewText)) {
            scoreText.check(matches(withSubstring("Correct! Score: ")));
        } else {
            scoreText.check(matches(withSubstring("Wrong! Score: ")));
        }
    }
}