package com.emmaguy.audiocastradio.base

import android.content.res.Resources
import com.emmaguy.audiocastradio.api.AudioStreamsApi
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.AnalyticsService
import com.emmaguy.audiocastradio.feature.CastManager
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionManagerListener
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject

interface BaseComponent {
    val res: Resources

    val uiScheduler: Scheduler
    val ioScheduler: Scheduler

    val audioStreamApi: AudioStreamsApi

    val analyticsService: AnalyticsService

    val onCastStateChanged: BehaviorSubject<CastState>

    val castManager: CastManager
    val optionsProvider: OptionsProvider
    val castSession: SessionManagerListener<CastSession>
}
