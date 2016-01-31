package com.emmaguy.audiocastradio.features.audiostream

import com.emmaguy.audiocastradio.base.AbstractPresenter
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Observable

class AudioStreamListPresenter(val audioStreams: List<AudioStream>,
                               val onCastCapabilityInitialised: BehaviorRelay<Unit>)
: AbstractPresenter<AudioStreamListPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setAudioStreams(audioStreams)

        unsubscribeOnDetach(view.onAudioStreamClicked()
                .skipUntil(onCastCapabilityInitialised)
                .subscribe({ audioStream -> view.startStream(audioStream) }))
    }

    interface View : AbstractPresenter.View {
        fun setAudioStreams(audioStreams: List<AudioStream>)

        fun onAudioStreamClicked(): Observable<AudioStream>

        fun startStream(audioStream: AudioStream)
    }
}