package com.emmaguy.audiocastradio.feature.audiostream

import com.emmaguy.audiocastradio.api.AudioStreamsApi
import com.emmaguy.audiocastradio.base.BasePresenter
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.AnalyticsService
import com.emmaguy.audiocastradio.feature.CastManager
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.*

class AudioStreamListPresenter(val uiScheduler: Scheduler,
                               val ioScheduler: Scheduler,
                               val audioStreamsApi: AudioStreamsApi,
                               val onCastStateChanged: Subject<CastState>,
                               val castManager: CastManager,
                               val analytics: AnalyticsService)
    : BasePresenter<AudioStreamListPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        unsubscribeOnDetach(Observable.just(Unit)
                .doOnNext { view.showLoading() }
                .flatMap { audioStreamsApi.audioStreams().subscribeOn(ioScheduler) }
                .observeOn(uiScheduler)
                .map { it.sortedWith(Comparator { t, t1 -> t.name.compareTo(t1.name) }) }
                .doOnNext { view.hideLoading() }
                .subscribe({ view.setAudioStreams(it) }))

        unsubscribeOnDetach(Observable.combineLatest(view.onAudioStreamClicked(), onCastStateChanged,
                BiFunction { audioStream: AudioStream, castState: CastState -> Pair(castState, audioStream) })
                .observeOn(uiScheduler)
                .subscribe({ pair: Pair<CastState, AudioStream> ->
                    val castState = pair.first
                    val audioStream = pair.second

                    Timber.d("emmaisawesome castState: " + castState)

                    if (castState == CastState.CONNECTED) {
                        analytics.logStartCastingEvent(audioStream)
                        castManager.loadStream(audioStream)
                    }
                }, { throwable: Throwable -> Timber.e(throwable, "Failure when stream clicked/cast state changed") }))
    }

    interface View : BasePresenter.View {
        fun showLoading()
        fun hideLoading()

        fun setAudioStreams(audioStreams: List<AudioStream>)

        fun onAudioStreamClicked(): Observable<AudioStream>
    }
}

