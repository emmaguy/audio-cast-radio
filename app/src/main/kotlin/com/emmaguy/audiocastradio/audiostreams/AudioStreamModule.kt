package com.emmaguy.audiocastradio.audiostreams

import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager

class AudioStreamModule {
    companion object {
        private val audioStreamsPresenter = AudioStreamsPresenter()

        fun presenter(): AudioStreamsPresenter {
            return audioStreamsPresenter
        }

        fun castManager(): VideoCastManager {
            return VideoCastManager.getInstance()
        }
    }
}