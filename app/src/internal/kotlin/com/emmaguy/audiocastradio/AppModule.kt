package com.emmaguy.audiocastradio

import android.content.Context
import android.view.Menu
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.feature.CastManager

class AppComponentImpl(context: Context) : BaseAppComponent(context) {
    override val castManager = object : CastManager {
        override fun loadStream(audioStream: AudioStream) {

        }

        override fun play() {

        }

        override fun pause() {

        }

        override fun incrementUiCounter() {

        }

        override fun decrementUiCounter() {

        }

        override fun addMediaRouterButton(menu: Menu, menuItemId: Int) {

        }
    }
}