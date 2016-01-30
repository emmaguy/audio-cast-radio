package com.emmaguy.audiocastradio.features.audiostream

import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager

internal class AudioStreamListModule {
    companion object {
        private val audioStreamsPresenter = AudioStreamListPresenter(audioStreams())

        private fun audioStreams(): List<AudioStream> {
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

        fun presenter(): AudioStreamListPresenter {
            return audioStreamsPresenter
        }

        fun castManager(): VideoCastManager {
            return VideoCastManager.getInstance()
        }
    }
}