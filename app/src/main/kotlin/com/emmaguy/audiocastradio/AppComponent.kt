package com.emmaguy.audiocastradio

import android.content.Context
import android.os.Bundle
import android.view.Menu
import com.emmaguy.audiocastradio.api.AudioStreamsApi
import com.emmaguy.audiocastradio.base.BaseComponent
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.AudioStreamJsonAdapter
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.AnalyticsService
import com.emmaguy.audiocastradio.feature.CastManager
import com.emmaguy.audiocastradio.feature.CastOptionsProvider
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber


class AppComponent(context: Context) : BaseComponent {
    override val audioStreamApi: AudioStreamsApi
        get() {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/emmaguy/audio-cast-radio/master/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                            .add(AudioStreamJsonAdapter())
                            .build()))
                    .build()

            return retrofit.create<AudioStreamsApi>(AudioStreamsApi::class.java)
        }

    override val res = context.resources
    override val ioScheduler = Schedulers.io()
    override val uiScheduler = AndroidSchedulers.mainThread()
    override val analyticsService: AnalyticsService = object : AnalyticsService {
        override fun logStartCastingEvent(audioStream: AudioStream) {
            val bundle = Bundle()
            bundle.putString(Param.ITEM_ID, audioStream.streamUrl)
            bundle.putString(Param.ITEM_NAME, audioStream.name)
            FirebaseAnalytics.getInstance(context).logEvent(Event.SELECT_CONTENT, bundle)
        }
    }

    override val onCastStateChanged: BehaviorSubject<CastState> = BehaviorSubject.create()

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
//            onCastStateChanged.onNext(CastState.fromId(castContext!!.castState))
            onCastStateChanged.onNext(CastState.CONNECTED)
        }

        override fun onSessionResuming(castSession: CastSession?, sessionId: String?) {
            Timber.d("onSessionResuming")
            onCastStateChanged.onNext(CastState.CONNECTING)
        }

        override fun onSessionEnded(castSession: CastSession?, error: Int) {
            Timber.d("onSessionEnded")
            onCastStateChanged.onNext(CastState.NOT_CONNECTED)
        }

        override fun onSessionStartFailed(castSession: CastSession?, error: Int) {
            Timber.d("onSessionStartFailed")
            onCastStateChanged.onNext(CastState.NOT_CONNECTED)
        }

        override fun onSessionStarting(castSession: CastSession?) {
            Timber.d("onSessionStarting")
            onCastStateChanged.onNext(CastState.CONNECTING)
        }

        override fun onSessionSuspended(castSession: CastSession?, reason: Int) {
            Timber.d("onSessionSuspended")
            onCastStateChanged.onNext(CastState.NOT_CONNECTED)
        }

        override fun onSessionStarted(castSession: CastSession?, error: String?) {
            Timber.d("onSessionStarted")
            onCastStateChanged.onNext(CastState.CONNECTED)
        }

        override fun onSessionEnding(castSession: CastSession?) {
            Timber.d("onSessionEnding")
            onCastStateChanged.onNext(CastState.NOT_CONNECTED)
        }

        override fun onSessionResumeFailed(castSession: CastSession?, error: Int) {
            Timber.d("onSessionResumeFailed")
            onCastStateChanged.onNext(CastState.NOT_CONNECTED)
        }
    }
}