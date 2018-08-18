package com.emmaguy.audiocastradio.feature.audiostream

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.runner.screenshot.Screenshot
import com.emmaguy.audiocastradio.di.Inject
import com.emmaguy.audiocastradio.di.Injector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioStreamListActivityTest : Injector by Inject.instance {
    @Rule @JvmField val rule = ActivityTestRule(AudioStreamListActivity::class.java)

    @Test fun takeScreenshot() {
        Screenshot.capture(rule.activity)
    }
}