package com.emmaguy.audiocastradio

import android.content.Context
import android.content.res.Resources
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers

interface AppComponent {
    val res: Resources

    val uiScheduler: Scheduler

    val onCastStateChanged: BehaviorRelay<CastState>

    val audioStreams: List<AudioStream>
    val castManager: CastManager
}

abstract class BaseAppComponent(context: Context): AppComponent {
    override val res = context.resources
    override val uiScheduler = AndroidSchedulers.mainThread()

    override val onCastStateChanged: BehaviorRelay<CastState> = BehaviorRelay.create()

    override val audioStreams = listOf(
            AudioStream("Klara",
                    "http://mp3.streampower.be/klara-high.mp3",
                    "http://www.radioviainternet.be/images/logos/logo-klara.png"),
            AudioStream("Nostalgie",
                    "http://nostalgiewhatafeeling.ice.infomaniak.ch/nostalgiewhatafeeling-128.mp3",
                    "http://www.nostalgie.be/radioplayer/img/radios/premium.png"),
            AudioStream("Radio 1",
                    "http://mp3.streampower.be/radio1-high.mp3",
                    "http://www.radio1.be/sites/all/themes/benny/logo.png"),
            AudioStream("Studio Brussel",
                    "http://mp3.streampower.be/stubru-high.mp3",
                    "http://cds.stubru.be/sites/all/themes/custom/netsites_admin/images/logos/logo.png"))
}