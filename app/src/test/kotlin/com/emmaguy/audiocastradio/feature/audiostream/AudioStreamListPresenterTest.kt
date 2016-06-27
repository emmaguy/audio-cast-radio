package com.emmaguy.audiocastradio.feature.audiostream

import com.emmaguy.audiocastradio.base.BasePresenterTest
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.PublishRelay
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import rx.schedulers.Schedulers

class AudioStreamListPresenterTest : BasePresenterTest<AudioStreamListPresenter, AudioStreamListPresenter.View>() {
    private val onCastStateChanged: BehaviorRelay<CastState> = BehaviorRelay.create()
    private val audioStreamClicked: PublishRelay<AudioStream> = PublishRelay.create()

    @Mock private val castManager: CastManager? = null

    override fun createPresenter(): AudioStreamListPresenter {
        return AudioStreamListPresenter(Schedulers.immediate(),
                listOf(AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL)),
                onCastStateChanged,
                castManager!!)
    }

    override fun createView(): AudioStreamListPresenter.View {
        val view = mock(AudioStreamListPresenter.View::class.java)
        `when`(view.onAudioStreamClicked()).thenReturn(audioStreamClicked)
        return view
    }

    @Test fun onAttachView_setsAudioStreams() {
        presenterOnAttachView()

        verify(getView()).setAudioStreams(anyObject())
        verify(castManager, never())?.loadStream(anyObject())
    }

    @Test fun casting_noAudioStream_doesNothingToCastManager() {
        presenterOnAttachView()

        onCastStateChanged.call(CastState(true))

        verifyZeroInteractions(castManager)
    }

    @Test fun audioStreamChanged_notCasting_doesNothingToCastManager() {
        presenterOnAttachView()

        audioStreamClicked.call(null)

        verifyZeroInteractions(castManager)
    }

    @Test fun onTogglePlayPauseAudioStreamClickedAndConnected_doesNothingToCastManager() {
        presenterOnAttachView()

        val audioStream = AudioStream("title", "stream", "img")
        audioStreamClicked.call(audioStream)
        onCastStateChanged.call(CastState(true))

        verify(castManager!!).loadStream(audioStream)
    }

    companion object {
        private val DEFAULT_TITLE = "title"
        private val DEFAULT_URL = "url"
        private val DEFAULT_IMAGE_URL = "image_url"
    }
}