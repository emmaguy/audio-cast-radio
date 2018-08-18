package com.emmaguy.castradio.feature.audiostream

import com.emmaguy.castradio.api.AudioStreamsApi
import com.emmaguy.castradio.base.BasePresenterTest
import com.emmaguy.castradio.data.AudioStream
import com.emmaguy.castradio.data.CastState
import com.emmaguy.castradio.feature.AnalyticsService
import com.emmaguy.castradio.feature.CastManager
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class AudioStreamListPresenterTest : BasePresenterTest<AudioStreamListPresenter, AudioStreamListPresenter.View>() {
    private val DEFAULT_NAME = "title"
    private val DEFAULT_URL = "url"
    private val DEFAULT_IMAGE_URL = "image_url"

    private val onCastStateChanged: PublishSubject<CastState> = PublishSubject.create()
    private val audioStreamClicked: PublishSubject<AudioStream> = PublishSubject.create()

    @Mock private val audioStreamsApi: AudioStreamsApi? = null
    @Mock private val castManager: CastManager? = null
    @Mock private val analyticsService: AnalyticsService? = null

    override fun createPresenter(): AudioStreamListPresenter {
        whenever(audioStreamsApi!!.audioStreams()).thenReturn(Observable.just(listOf(AudioStream(DEFAULT_NAME,
                DEFAULT_URL, DEFAULT_IMAGE_URL))))

        return AudioStreamListPresenter(Schedulers.trampoline(),
                Schedulers.trampoline(),
                audioStreamsApi,
                onCastStateChanged,
                castManager!!,
                analyticsService!!)
    }

    override fun createView(): AudioStreamListPresenter.View {
        val view = mock(AudioStreamListPresenter.View::class.java)
        whenever(view.onAudioStreamClicked()).thenReturn(audioStreamClicked)
        return view
    }

    @Test fun onAttachView_setsAudioStreams() {
        presenterOnAttachView()

        verify(getView()).setAudioStreams(anyObject())
        verify(castManager, never())?.loadStream(anyObject())
    }

    @Test fun casting_noAudioStream_doesNothingToCastManager() {
        presenterOnAttachView()

        onCastStateChanged.onNext(CastState.CONNECTED)

        verifyZeroInteractions(castManager)
    }

    @Test fun audioStreamChanged_notCasting_doesNothingToCastManager() {
        presenterOnAttachView()

        audioStreamClicked.onNext(AudioStream(DEFAULT_NAME, DEFAULT_URL, DEFAULT_IMAGE_URL))

        verifyZeroInteractions(castManager)
    }

    @Test fun onTogglePlayPauseAudioStreamClickedAndConnected_doesNothingToCastManager() {
        presenterOnAttachView()

        val audioStream = AudioStream(DEFAULT_NAME, DEFAULT_URL, DEFAULT_IMAGE_URL)
        audioStreamClicked.onNext(audioStream)
        onCastStateChanged.onNext(CastState.CONNECTED)

        verify(castManager!!).loadStream(audioStream)
    }
}