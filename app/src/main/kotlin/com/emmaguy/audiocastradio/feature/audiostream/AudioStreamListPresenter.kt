package com.emmaguy.audiocastradio.feature.audiostream

import com.emmaguy.audiocastradio.base.BasePresenter
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.Subject
import timber.log.Timber

class AudioStreamListPresenter(val uiScheduler: Scheduler,
                               val audioStreams: List<AudioStream>,
                               val onCastStateChanged: Subject<CastState>,
                               val castManager: CastManager)
    : BasePresenter<AudioStreamListPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setAudioStreams(audioStreams)

        unsubscribeOnDetach(Observable.combineLatest(view.onAudioStreamClicked(), onCastStateChanged,
                BiFunction { audioStream: AudioStream, castState: CastState -> Pair(castState, audioStream) })
                .observeOn(uiScheduler)
                .subscribe({ pair: Pair<CastState, AudioStream> ->
                    val castState = pair.first
                    val audioStream = pair.second

                    if (castState.isConnected) {
                        castManager.loadStream(audioStream)
                    }
                }, { throwable: Throwable -> Timber.e(throwable, "Failure when stream clicked/cast state changed") }))
    }

    interface View : BasePresenter.View {
        fun setAudioStreams(audioStreams: List<AudioStream>)

        fun onAudioStreamClicked(): Observable<AudioStream>
    }
}