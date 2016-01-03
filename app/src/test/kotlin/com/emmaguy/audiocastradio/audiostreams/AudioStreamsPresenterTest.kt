package com.emmaguy.audiocastradio.audiostreams

import com.emmaguy.audiocastradio.base.AbstractPresenterTest
import com.jakewharton.rxrelay.PublishRelay
import org.junit.Test
import org.mockito.Mockito
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

        val mockAudioStream = AudioStream("title", "url", "imgurl")
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(mockAudioStream)

        verify(getView()).startStream(mockAudioStream)
    }

    @Test fun onAudioStreamClicked_whenStartStreamWithSameObjectAlreadyCalled_doesNotCallStartStreamAgain() {
        presenterOnAttachView()

        val audioStream = AudioStream("title", "url", "imgurl")
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)
        audioStreamClicked.call(audioStream)

        verify(getView()).startStream(audioStream)
    }

    @Test fun onAudioStreamClicked_whenStartStreamWithDifferentAudioStream_startsStreamWithNewAudioStream() {
        presenterOnAttachView()

        val audioStream = AudioStream("title", "url", "imgurl")
        castCapabilityInitialised.call(Unit)
        audioStreamClicked.call(audioStream)

        val differentAudioStream = AudioStream("title", "different url", "imgurl")
        audioStreamClicked.call(differentAudioStream)

        verify(getView()).startStream(audioStream)
        verify(getView()).startStream(audioStream)
    }

    @Test fun onAudioStreamClicked_whenCastCapabilityNotInitialisedYet_doesNotStartStream() {
        presenterOnAttachView()

        val mockAudioStream = AudioStream("title", "url", "imgurl")
        audioStreamClicked.call(mockAudioStream)
        castCapabilityInitialised.call(Unit)

        verify(getView(), never()).startStream(mockAudioStream)
    }

    private fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}