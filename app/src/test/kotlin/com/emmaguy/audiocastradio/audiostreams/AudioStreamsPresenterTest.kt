package com.emmaguy.audiocastradio.audiostreams

import com.emmaguy.audiocastradio.base.AbstractPresenterTest
import com.jakewharton.rxrelay.PublishRelay
import org.junit.Test
import org.mockito.Mockito.*

class AudioStreamsPresenterTest : AbstractPresenterTest<AudioStreamsPresenter, AudioStreamsPresenter.View>() {
    private val castCapabilityInitialised: PublishRelay<Unit> = PublishRelay.create()
    private val audioStreamClicked: PublishRelay<AudioStream> = PublishRelay.create()

    override fun createPresenter(): AudioStreamsPresenter {
        return AudioStreamsPresenter()
    }

    override fun createView(): AudioStreamsPresenter.View {
        val view = mock(AudioStreamsPresenter.View::class.java)
        `when`(view.onAudioStreamClicked()).thenReturn(audioStreamClicked)
        `when`(view.onCastCapabilityInitialised()).thenReturn(castCapabilityInitialised)
        return view
    }

    @Test fun onAttachView_setsAudioStreams() {
        presenterOnAttachView()

        verify(getView()).setAudioStreams(anyObject())
        verify(getView(), never()).startStream(anyObject())
    }

    @Test fun onAudioStreamClickedAfterCastCapabilityInitialised_startsStream() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL)
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)

        verify(getView()).startStream(audioStream)
    }

    @Test fun onAudioStreamClicked_whenStartStreamWithSameObjectAlreadyCalled_callsStartStreamAgain() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL)
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)
        audioStreamClicked.call(audioStream)

        verify(getView(), times(2)).startStream(audioStream)
    }

    @Test fun onAudioStreamClicked_whenStartStreamWithDifferentAudioStream_startsStreamWithNewAudioStream() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL)
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)

        val newAudioStream = AudioStream(DEFAULT_TITLE, "new url", DEFAULT_IMAGE_URL)
        audioStreamClicked.call(newAudioStream)

        verify(getView()).startStream(audioStream)
        verify(getView()).startStream(newAudioStream)
    }

    @Test fun onAudioStreamClicked_whenCastCapabilityNotInitialisedYet_doesNotStartStream() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_TITLE, DEFAULT_URL, DEFAULT_IMAGE_URL)
        audioStreamClicked.call(audioStream)
        castCapabilityInitialised.call(Unit)

        verify(getView(), never()).startStream(audioStream)
    }

    companion object {
        private val DEFAULT_TITLE = "title"
        private val DEFAULT_URL = "url"
        private val DEFAULT_IMAGE_URL = "image_url"
    }
}