package com.emmaguy.audiocastradio.base

import android.content.res.Resources
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionManagerListener
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Scheduler

interface BaseComponent {
    val res: Resources

    val uiScheduler: Scheduler

    val onCastStateChanged: BehaviorRelay<CastState>

    val audioStreams: List<AudioStream>

    val castManager: CastManager
    val optionsProvider: OptionsProvider
    val castSession: SessionManagerListener<CastSession>
}
