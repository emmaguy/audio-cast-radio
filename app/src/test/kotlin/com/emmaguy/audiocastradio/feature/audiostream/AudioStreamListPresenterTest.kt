package com.emmaguy.audiocastradio.feature.audiostream

import com.emmaguy.audiocastradio.base.BasePresenterTest
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.feature.CastManager
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class AudioStreamListPresenterTest : BasePresenterTest<AudioStreamListPresenter, AudioStreamListPresenter.View>() {
    private val onCastStateChanged: BehaviorSubject<CastState> = BehaviorSubject.create()
    private val audioStreamClicked: PublishSubject<AudioStream> = PublishSubject.create()

    @Mock private val castManager: CastManager? = null

    override fun createPresenter(): AudioStreamListPresenter {
        return AudioStreamListPresenter(Schedulers.trampoline(),
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

        onCastStateChanged.onNext(CastState(true))

        verifyZeroInteractions(castManager)
    }

    @Test fun audioStreamChanged_notCasting_doesNothingToCastManager() {
        presenterOnAttachView()

        audioStreamClicked.onNext(null)

        verifyZeroInteractions(castManager)
    }

    @Test fun onTogglePlayPauseAudioStreamClickedAndConnected_doesNothingToCastManager() {
        presenterOnAttachView()

        val audioStream = AudioStream("title", "stream", "img")
        audioStreamClicked.onNext(audioStream)
        onCastStateChanged.onNext(CastState(true))

        verify(castManager!!).loadStream(audioStream)
    }

    companion object {
        private val DEFAULT_TITLE = "title"
        private val DEFAULT_URL = "url"
        private val DEFAULT_IMAGE_URL = "image_url"
    }
}