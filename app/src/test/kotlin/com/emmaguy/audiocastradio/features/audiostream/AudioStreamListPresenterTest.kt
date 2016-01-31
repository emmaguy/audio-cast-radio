package com.emmaguy.audiocastradio.features.audiostream

import com.emmaguy.audiocastradio.base.AbstractPresenterTest
import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.PublishRelay
import org.junit.Test
import org.mockito.Mockito.*

class AudioStreamListPresenterTest : AbstractPresenterTest<AudioStreamListPresenter, AudioStreamListPresenter.View>() {
    private val castCapabilityInitialised: BehaviorRelay<Unit> = BehaviorRelay.create()
    private val audioStreamClicked: PublishRelay<AudioStream> = PublishRelay.create()

    override fun createPresenter(): AudioStreamListPresenter {
        return AudioStreamListPresenter(listOf(AudioStream(DEFAULT_TITLE, DEFAULT_URL)), castCapabilityInitialised)
    }

    override fun createView(): AudioStreamListPresenter.View {
        val view = mock(AudioStreamListPresenter.View::class.java)
        `when`(view.onAudioStreamClicked()).thenReturn(audioStreamClicked)
        return view
    }

    @Test fun onAttachView_setsAudioStreams() {
        presenterOnAttachView()

        verify(getView()).setAudioStreams(anyObject())
        verify(getView(), never()).startStream(anyObject())
    }

    @Test fun onAudioStreamClickedAfterCastCapabilityInitialised_startsStream() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL)
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)

        verify(getView()).startStream(audioStream)
    }

    @Test fun onAudioStreamClicked_whenStartStreamWithSameObjectAlreadyCalled_callsStartStreamAgain() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL)
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)
        audioStreamClicked.call(audioStream)

        verify(getView(), times(2)).startStream(audioStream)
    }

    @Test fun onAudioStreamClicked_whenStartStreamWithDifferentAudioStream_startsStreamWithNewAudioStream() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL)
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)

        val newAudioStream = AudioStream(DEFAULT_TITLE, "new url")
        audioStreamClicked.call(newAudioStream)

        verify(getView()).startStream(audioStream)
        verify(getView()).startStream(newAudioStream)
    }

    @Test fun onAudioStreamClicked_whenCastCapabilityNotInitialisedYet_doesNotStartStream() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL)
        audioStreamClicked.call(audioStream)
        castCapabilityInitialised.call(Unit)

        verify(getView(), never()).startStream(audioStream)
    }

    companion object {
        private val DEFAULT_TITLE = "title"
        private val DEFAULT_URL = "url"
    }
}