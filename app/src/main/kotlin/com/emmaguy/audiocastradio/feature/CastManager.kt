package com.emmaguy.audiocastradio.feature

import android.support.annotation.MenuRes
import android.view.Menu
import com.emmaguy.audiocastradio.data.AudioStream

interface CastManager {
    fun loadStream(audioStream: AudioStream)

    fun play()
    fun pause()

    fun incrementUiCounter()
    fun decrementUiCounter()

    fun addMediaRouterButton(menu: Menu, @MenuRes menuItemId: Int)
}