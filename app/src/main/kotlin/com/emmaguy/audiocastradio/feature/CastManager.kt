package com.emmaguy.audiocastradio.feature

import android.content.Context
import android.support.annotation.MenuRes
import android.view.Menu
import com.emmaguy.audiocastradio.data.AudioStream

interface CastManager {
    fun init(context: Context)
    fun setUpMediaRouteButton(menu: Menu, @MenuRes menuItemId: Int)

    fun loadStream(audioStream: AudioStream)

    fun onResume()
    fun onPause()
}