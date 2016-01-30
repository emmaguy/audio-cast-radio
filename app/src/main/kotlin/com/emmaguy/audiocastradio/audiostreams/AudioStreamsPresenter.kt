package com.emmaguy.audiocastradio.audiostreams

import com.emmaguy.audiocastradio.base.AbstractPresenter
import rx.Observable

class AudioStreamsPresenter() : AbstractPresenter<AudioStreamsPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setAudioStreams(buildAudioStreams())

        unsubscribeOnDetach(view.onAudioStreamClicked()
                .skipUntil(view.onCastCapabilityInitialised())
                .subscribe({ audioStream -> view.startStream(audioStream) }))
    }

    private fun buildAudioStreams(): List<AudioStream> {
        return listOf(
                AudioStream("Klara",
                        "http://mp3.streampower.be/klara-high.mp3",
                        "http://www.radioviainternet.be/images/logos/logo-klara.png"),
                AudioStream("Nostalgie",
                        "http://nostalgiewhatafeeling.ice.infomaniak.ch/nostalgiewhatafeeling-128.mp3",
                        "http://www.nostalgie.be/radioplayer/img/visual-placeholder.png"),
                AudioStream("Radio 1",
                        "http://mp3.streampower.be/radio1-high.mp3",
                        "http://www.radio1.be/sites/all/themes/benny/logo.png"),
                AudioStream("Studio Brussel",
                        "http://mp3.streampower.be/stubru-high.mp3",
                        "http://cds.stubru.be/sites/all/themes/custom/netsites_admin/images/logos/logo.png"))
    }

    interface View : AbstractPresenter.View {
        fun setAudioStreams(audioStreams: List<AudioStream>)

        fun onCastCapabilityInitialised(): Observable<Unit>
        fun onAudioStreamClicked(): Observable<AudioStream>

        fun startStream(audioStream: AudioStream)
    }
}