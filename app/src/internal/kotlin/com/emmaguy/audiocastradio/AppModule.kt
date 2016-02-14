package com.emmaguy.audiocastradio

import android.view.Menu
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import com.jakewharton.rxrelay.BehaviorRelay
import rx.android.schedulers.AndroidSchedulers

class AppModule(val app: App) {
    val onCastStateChanged: BehaviorRelay<CastState> = BehaviorRelay.create()
    val resources = app.resources
    val uiScheduler = AndroidSchedulers.mainThread()
    val castManager: CastManager = object : CastManager {
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