package com.project.hotel.main;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.project.hotel.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by melbehiry on 7/10/17.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    @Rule
    ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);



    @Test
    public void testMainActivity(){
        //get context

        Context context = InstrumentationRegistry.getContext();

        onView(withId(R.id.search_drawer)).perform(click());


    }





}