package com.emmaguy.audiocastradio

import android.content.Context
import android.view.Menu
import com.emmaguy.audiocastradio.base.BaseComponent
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import com.emmaguy.audiocastradio.feature.CastOptionsProvider
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.jakewharton.rxrelay.BehaviorRelay
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber

class AppComponent(context: Context) : BaseComponent {
    override val res = context.resources
    override val uiScheduler = AndroidSchedulers.mainThread()

    override val onCastStateChanged: BehaviorRelay<CastState> = BehaviorRelay.create()

    override val audioStreams = listOf(
            AudioStream("Klara",
                    "http://mp3.streampower.be/klara-high.mp3",
                    "https://static-media.streema.com/media/cache/a6/1f/a61fdf7e8d64646b773345e8a7e1d56e.jpg"),
            AudioStream("Nostalgie",
                    "http://nostalgiewhatafeeling.ice.infomaniak.ch/nostalgiewhatafeeling-128.mp3",
                    "http://www.nostalgie.be/radioplayer/img/radios/premium.png"),
            AudioStream("Radio 1",
                    "http://mp3.streampower.be/radio1-high.mp3",
                    "http://logowow.net/logos/thumb-70ASyo6ci.png"),
            AudioStream("Studio Brussel",
                    "http://mp3.streampower.be/stubru-high.mp3",
                    "http://stubru.be/favicon.png"))
    var castContext: CastContext? = null

    override val optionsProvider = CastOptionsProvider()

    override val castManager: CastManager = object : CastManager {
        override fun init(context: Context) {
            castContext = CastContext.getSharedInstance(context)
        }

        override fun setUpMediaRouteButton(menu: Menu, menuItemId: Int) {
            CastButtonFactory.setUpMediaRouteButton(context, menu, menuItemId)
        }

        override fun loadStream(audioStream: AudioStream) {
            castContext?.sessionManager?.currentCastSession?.remoteMediaClient?.load(audioStream.toMediaInfo())
        }

        override fun onResume() {
            castContext?.sessionManager?.addSessionManagerListener(castSession, CastSession::class.java)
        }

        override fun onPause() {
            castContext?.sessionManager?.removeSessionManagerListener(castSession, CastSession::class.java)
        }
    }

    override val castSession: SessionManagerListener<CastSession> = object : SessionManagerListener<CastSession> {
        override fun onSessionResumed(castSession: CastSession?, wasSuspended: Boolean) {
            Timber.d("onSessionResumed")
            onCastStateChanged.call(CastState(true))
        }

        override fun onSessionResuming(castSession: CastSession?, sessionId: String?) {
            Timber.d("onSessionResuming")
            onCastStateChanged.call(CastState(false))
        }

        override fun onSessionEnded(castSession: CastSession?, error: Int) {
            Timber.d("onSessionEnded")
            onCastStateChanged.call(CastState(false))
        }

        override fun onSessionStartFailed(castSession: CastSession?, error: Int) {
            Timber.d("onSessionStartFailed")
            onCastStateChanged.call(CastState(false))
        }

        override fun onSessionStarting(castSession: CastSession?) {
            Timber.d("onSessionStarting")
            onCastStateChanged.call(CastState(false))
        }

        override fun onSessionSuspended(castSession: CastSession?, reason: Int) {
            Timber.d("onSessionSuspended")
            onCastStateChanged.call(CastState(false))
        }

        override fun onSessionStarted(castSession: CastSession?, error: String?) {
            Timber.d("onSessionStarted")
            onCastStateChanged.call(CastState(true))
        }

        override fun onSessionEnding(castSession: CastSession?) {
            Timber.d("onSessionEnding")
            onCastStateChanged.call(CastState(false))
        }

        override fun onSessionResumeFailed(castSession: CastSession?, error: Int) {
            Timber.d("onSessionResumeFailed")
            onCastStateChanged.call(CastState(false))
        }
    }
}