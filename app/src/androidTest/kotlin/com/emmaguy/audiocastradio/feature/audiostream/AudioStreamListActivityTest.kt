package com.emmaguy.audiocastradio.feature.audiostream

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.emmaguy.audiocastradio.di.Inject
import com.emmaguy.audiocastradio.di.Injector
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioStreamListActivityTest : Injector by Inject.instance {
    @Rule @JvmField val rule = ActivityTestRule(AudioStreamListActivity::class.java)

    @Test fun takeScreenshot() {
        ScreenShotter.takeScreenshot("audio_stream_list_idle", rule.activity)
    }
}