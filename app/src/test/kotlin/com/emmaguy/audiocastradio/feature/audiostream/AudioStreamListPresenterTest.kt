package com.emmaguy.audiocastradio.feature.audiostream

import com.emmaguy.audiocastradio.feature.CastManager
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.data.MediaState
import com.emmaguy.audiocastradio.base.AbstractPresenterTest
import com.emmaguy.audiocastradio.data.AudioStream
import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.PublishRelay
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import rx.schedulers.Schedulers

class AudioStreamListPresenterTest : AbstractPresenterTest<AudioStreamListPresenter, AudioStreamListPresenter.View>() {
    private val onCastStateChanged: BehaviorRelay<CastState> = BehaviorRelay.create()

    private val audioStreamClicked: PublishRelay<AudioStream> = PublishRelay.create()
    private val togglePlayPauseAudioStreamClicked: PublishRelay<Unit> = PublishRelay.create()

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
        `when`(view.onTogglePlayPauseAudioStreamClicked()).thenReturn(togglePlayPauseAudioStreamClicked)
        return view
    }

    @Test fun onAttachView_setsAudioStreams() {
        presenterOnAttachView()

        verify(getView()).setAudioStreams(anyObject())
        verify(castManager, never())?.loadStream(anyObject())
    }

    @Test fun whenConnectedAndIdleAndStreamClicked_loadsStreamAndShowLoading() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL)

        onCastStateChanged.call(CastState(true, MediaState.IDLE, null, null))
        audioStreamClicked.call(audioStream)

        verify(getView()).showLoadingView()
        verify(castManager)?.loadStream(audioStream)
    }

    @Test fun whenConnectedAndPlayingAndStreamClicked_doesNotLoadStream() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL)

        onCastStateChanged.call(CastState(true, MediaState.PLAYING, null, null))
        audioStreamClicked.call(audioStream)

        verify(castManager, never())?.loadStream(anyObject())
    }

    @Test fun whenConnectedAndPlayingAndStreamClicked_hidesLoadingViewAndShowPauseStreamView() {
        presenterOnAttachView()

        audioStreamClicked.call(AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL))
        onCastStateChanged.call(CastState(true, MediaState.PLAYING, null, null))

        verify(getView()).hideLoadingView()
        verify(getView()).showPauseStreamView()
    }

    @Test fun whenConnectedAndPausedAndStreamClicked_showPlayStreamView() {
        presenterOnAttachView()

        audioStreamClicked.call(AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL))
        onCastStateChanged.call(CastState(true, MediaState.PAUSED, null, null))

        verify(getView()).showPlayStreamView()
    }

    @Test fun whenNotConnectedAndIdle_hidePlayPauseStreamView() {
        presenterOnAttachView()

        onCastStateChanged.call(CastState(false, MediaState.IDLE, null, null))

        verify(getView()).hidePlayPauseStreamView()
    }

    @Test fun whenNotConnectedAndInUnknownState_hidePlayPauseStreamView() {
        presenterOnAttachView()

        onCastStateChanged.call(CastState(false, MediaState.UNKNOWN, null, null))

        verify(getView()).hidePlayPauseStreamView()
    }

    @Test fun whenOnTogglePlayPauseAudioStreamClickedAndNotConnected_doesNothingToCastManager() {
        presenterOnAttachView()

        togglePlayPauseAudioStreamClicked.call(Unit)
        onCastStateChanged.call(CastState(false, MediaState.UNKNOWN, null, null))

        verifyZeroInteractions(castManager)
    }

    @Test fun whenOnTogglePlayPauseAudioStreamClickedAndConnectedAndIdle_doesNothingToCastManager() {
        presenterOnAttachView()

        togglePlayPauseAudioStreamClicked.call(Unit)
        onCastStateChanged.call(CastState(true, MediaState.IDLE, null, null))

        verifyZeroInteractions(castManager)
    }

    @Test fun whenOnTogglePlayPauseAudioStreamClickedAndConnectedAndUnknown_doesNothingToCastManager() {
        presenterOnAttachView()

        togglePlayPauseAudioStreamClicked.call(Unit)
        onCastStateChanged.call(CastState(true, MediaState.UNKNOWN, null, null))

        verifyZeroInteractions(castManager)
    }

    @Test fun whenOnTogglePlayPauseAudioStreamClickedAndConnectedAndBuffering_doesNothingToCastManager() {
        presenterOnAttachView()

        onCastStateChanged.call(CastState(true, MediaState.BUFFERING, null, null))
        togglePlayPauseAudioStreamClicked.call(Unit)

        verifyZeroInteractions(castManager)
    }

    @Test fun whenOnTogglePlayPauseAudioStreamConnectedAndPlaying_pausesOnCastManager() {
        presenterOnAttachView()

        onCastStateChanged.call(CastState(true, MediaState.PLAYING, null, null))
        togglePlayPauseAudioStreamClicked.call(Unit)

        verify(castManager)?.pause()
        verify(getView()).showPauseStreamView()
    }

    @Test fun whenOnTogglePlayPauseAudioStreamConnectedAndPaused_playsOnCastManager() {
        presenterOnAttachView()

        onCastStateChanged.call(CastState(true, MediaState.PAUSED, null, null))
        togglePlayPauseAudioStreamClicked.call(Unit)

        verify(castManager)?.play()
        verify(getView()).showPlayStreamView()
    }

    companion object {
        private val DEFAULT_TITLE = "title"
        private val DEFAULT_URL = "url"
        private val DEFAULT_IMAGE_URL = "image_url"
    }
}