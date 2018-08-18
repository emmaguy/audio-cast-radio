package com.emmaguy.castradio.feature

import android.content.Context
import androidx.annotation.MenuRes
import android.view.Menu
import com.emmaguy.castradio.data.AudioStream

interface CastManager {
    fun init(context: Context)
    fun setUpMediaRouteButton(menu: Menu, @MenuRes menuItemId: Int)

    fun loadStream(audioStream: AudioStream)

    fun onResume()
    fun onPause()
}