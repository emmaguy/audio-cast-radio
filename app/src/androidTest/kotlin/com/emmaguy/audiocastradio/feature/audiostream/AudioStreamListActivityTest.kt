package com.emmaguy.audiocastradio.feature.audiostream

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.test.suitebuilder.annotation.LargeTest
import com.emmaguy.audiocastradio.App
import com.emmaguy.audiocastradio.R
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.data.MediaState
import com.squareup.spoon.Spoon
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AudioStreamListActivityTest {
    @Rule @JvmField val rule = ActivityTestRule(AudioStreamListActivity::class.java)

    @Test fun listOfAudioStreamsIsVisible() {
        onView(withId(R.id.audioStreamListRecyclerView)).check(matches(isDisplayed()));

        Spoon.screenshot(rule.activity, "audio_stream_list")
    }

    @Test fun onConnectedAndAudioStreamClicked_showLoadingUntilPlayingThenShowPause() {
        Spoon.screenshot(rule.activity, "audio_stream_list_idle")

        App.instance.appModule.onCastStateChanged.call(CastState(true, MediaState.IDLE, null, null))

        onView(withId(R.id.audioStreamListRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()));
        Spoon.screenshot(rule.activity, "audio_stream_list_post_loading")

        onView(withId(R.id.audioStreamListProgressBar)).check(matches(isDisplayed()));

        App.instance.appModule.onCastStateChanged.call(CastState(true, MediaState.PLAYING, null, null))
        Spoon.screenshot(rule.activity, "audio_stream_list_playing")

        onView(withId(R.id.audioStreamListProgressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.audioStreamListTogglePlayPauseButton)).check(matches(isDisplayed()));
    }
}