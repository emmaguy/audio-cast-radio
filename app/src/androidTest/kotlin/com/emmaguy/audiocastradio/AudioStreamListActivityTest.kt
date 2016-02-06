package com.emmaguy.audiocastradio

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import com.emmaguy.audiocastradio.features.audiostream.AudioStreamListActivity
import com.squareup.spoon.Spoon
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AudioStreamListActivityTest {
    @Rule @JvmField val rule = ActivityTestRule(AudioStreamListActivity::class.java)

    @Test fun listOfAudioStreamsIsVisible() {
        onView(withId(R.id.audioStreamsRecyclerView)).check(matches(allOf(isDisplayed())));

        Spoon.screenshot(rule.activity, "audio_stream_list")
    }
}