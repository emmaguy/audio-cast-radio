package com.emmaguy.audiocastradio.feature.audiostream

import com.emmaguy.audiocastradio.base.BasePresenter
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Observable
import rx.Scheduler
import timber.log.Timber

class AudioStreamListPresenter(val uiScheduler: Scheduler,
                               val audioStreams: List<AudioStream>,
                               val onCastStateChanged: BehaviorRelay<CastState>,
                               val castManager: CastManager)
: BasePresenter<AudioStreamListPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setAudioStreams(audioStreams)

        unsubscribeOnDetach(Observable.combineLatest(view.onAudioStreamClicked(), onCastStateChanged,
                { audioStream, castState -> Pair(castState, audioStream) })
                .observeOn(uiScheduler)
                .subscribe({ pair ->
                    val castState = pair.first
                    val audioStream = pair.second

                    if (castState.isConnected && audioStream != null) {
                        castManager.loadStream(audioStream)
                    }
                }, { throwable -> Timber.e(throwable, "Failure when stream clicked/cast state changed") }))
    }

    interface View : BasePresenter.View {
        fun setAudioStreams(audioStreams: List<AudioStream>)

        fun onAudioStreamClicked(): Observable<AudioStream>
    }
}