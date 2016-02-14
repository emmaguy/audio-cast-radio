package com.emmaguy.audiocastradio.feature.audiostream

import com.emmaguy.audiocastradio.base.AbstractPresenter
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.data.MediaState
import com.emmaguy.audiocastradio.feature.CastManager
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Observable
import rx.Scheduler
import timber.log.Timber

class AudioStreamListPresenter(val uiScheduler: Scheduler,
                               val audioStreams: List<AudioStream>,
                               val onCastStateChanged: BehaviorRelay<CastState>,
                               val castManager: CastManager)
: AbstractPresenter<AudioStreamListPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setAudioStreams(audioStreams)

        unsubscribeOnDetach(view.onTogglePlayStopAudioStreamClicked()
                .subscribe({
                    val castState = onCastStateChanged.value
                    if (castState != null && castState.isConnected) {
                        if (castState.mediaState == MediaState.PLAYING) {
                            castManager.pause()
                        } else if (castState.mediaState == MediaState.PAUSED) {
                            castManager.play()
                        }
                    }
                }, { throwable -> Timber.e(throwable, "Failure when toggle play/stop clicked") }))

        val castStateChanged = onCastStateChanged
                .doOnNext { Timber.d("Cast state: " + it.mediaState + " is connected: " + it.isConnected) }
                .observeOn(uiScheduler)
                .doOnNext {
                    if (it.mediaState == MediaState.UNKNOWN || it.mediaState == MediaState.IDLE) {
                        view.hidePlayStopStreamView()
                    } else if (it.mediaState == MediaState.PLAYING) {
                        view.showPauseStreamView()
                    }
                }

        unsubscribeOnDetach(Observable.combineLatest(view.onAudioStreamClicked(), castStateChanged,
                { audioStream, castState -> Pair(castState, audioStream) })
                .observeOn(uiScheduler)
                .subscribe({ pair ->
                    val castState = pair.first
                    val audioStream = pair.second

                    if (castState.isConnected) {
                        if (castState.mediaState == MediaState.IDLE && audioStream != null) {
                            castManager.loadStream(audioStream)
                            view.showLoadingView()
                        } else if (castState.mediaState == MediaState.PLAYING) {
                            view.hideLoadingView()
                        } else if (castState.mediaState == MediaState.PAUSED) {
                            view.showPlayStreamView()
                        }
                    }
                }, { throwable -> Timber.e(throwable, "Failure when trying to start audio stream") }))
    }

    interface View : AbstractPresenter.View {
        fun setAudioStreams(audioStreams: List<AudioStream>)

        fun showLoadingView()
        fun hideLoadingView()

        fun showPauseStreamView()
        fun showPlayStreamView()
        fun hidePlayStopStreamView()

        fun onAudioStreamClicked(): Observable<AudioStream>
        fun onTogglePlayStopAudioStreamClicked(): Observable<Unit>
    }
}