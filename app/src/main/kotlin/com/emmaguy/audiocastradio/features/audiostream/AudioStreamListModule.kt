package com.emmaguy.audiocastradio.features.audiostream

import com.emmaguy.audiocastradio.AppModule
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager

internal class AudioStreamListModule {
    companion object {
        internal val audioStreamsPresenter = AudioStreamListPresenter(audioStreams(), AppModule.onCastCapabilityInitialised)
        internal val castManager = VideoCastManager.getInstance()

        private fun audioStreams(): List<AudioStream> {
            return listOf(
                    AudioStream("Klara",
                            "http://mp3.streampower.be/klara-high.mp3"),
                    AudioStream("Nostalgie",
                            "http://nostalgiewhatafeeling.ice.infomaniak.ch/nostalgiewhatafeeling-128.mp3"),
                    AudioStream("Radio 1",
                            "http://mp3.streampower.be/radio1-high.mp3"),
                    AudioStream("Studio Brussel",
                            "http://mp3.streampower.be/stubru-high.mp3"))
        }
    }
}