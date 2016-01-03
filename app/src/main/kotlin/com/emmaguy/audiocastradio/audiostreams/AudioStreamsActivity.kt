package com.emmaguy.audiocastradio.audiostreams

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import com.emmaguy.audiocastradio.R
import com.emmaguy.audiocastradio.base.AbstractActivity
import com.emmaguy.audiocastradio.base.AbstractPresenter
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl
import com.jakewharton.rxrelay.PublishRelay
import kotlinx.android.synthetic.main.activity_audio_streams.*
import rx.Observable

class AudioStreamsActivity(val presenter: AudioStreamsPresenter = AudioStreamModule.presenter(),
                           val castManager: VideoCastManager = AudioStreamModule.castManager()) :
        AbstractActivity<AudioStreamsPresenter.View>(), AudioStreamsPresenter.View {
    private val castCapabilityInitialised: PublishRelay<Unit> = PublishRelay.create()
    private val audioStreamClicked: PublishRelay<AudioStream> = PublishRelay.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        castManager.addVideoCastConsumer(object : VideoCastConsumerImpl() {
            override fun onApplicationConnected(appMetadata: ApplicationMetadata, sessionId: String, wasLaunched: Boolean) {
                super.onApplicationConnected(appMetadata, sessionId, wasLaunched)

                if (appMetadata.isNamespaceSupported(NAMESPACE_MEDIA_PLAYBACK)) {
                    castCapabilityInitialised.call(Unit)
                }
            }

            override fun onRemoteMediaPlayerStatusUpdated() {
                super.onRemoteMediaPlayerStatusUpdated()

                if(castManager.isConnected && castManager.remoteMediaInformation != null && castManager.isRemoteMediaPlaying) {
                    castCapabilityInitialised.call(Unit)
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_audio_streams
    }

    override fun setAudioStreams(audioStreams: List<AudioStream>) {
        audioStreamsRecyclerView.setHasFixedSize(true)
        audioStreamsRecyclerView.adapter = AudioStreamsAdapter(audioStreams, audioStreamClicked)
        audioStreamsRecyclerView.itemAnimator = DefaultItemAnimator()
        audioStreamsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun startStream(audioStream: AudioStream) {
        castManager.loadMedia(audioStream.toMediaInfo(), true, 0)
    }

    override fun onCastCapabilityInitialised(): Observable<Unit> {
        return castCapabilityInitialised
    }

    override fun onAudioStreamClicked(): Observable<AudioStream> {
        return audioStreamClicked
    }

    override fun getPresenter(): AbstractPresenter<AudioStreamsPresenter.View> {
        return presenter
    }

    override fun getPresenterView(): AudioStreamsPresenter.View {
        return this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.cast_audio, menu)
        castManager.addMediaRouterButton(menu, R.id.cast_audio_menu_item)
        return true
    }

    override fun onResume() {
        super.onResume()
        castManager.incrementUiCounter()
    }

    override fun onPause() {
        castManager.decrementUiCounter()
        super.onPause()
    }

    companion object {
        private val NAMESPACE_MEDIA_PLAYBACK = "urn:x-cast:com.google.cast.media"
    }
}
