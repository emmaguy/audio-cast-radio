package com.emmaguy.audiocastradio.features.audiostream

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.emmaguy.audiocastradio.R
import com.emmaguy.audiocastradio.base.AbstractActivity
import com.emmaguy.audiocastradio.base.AbstractPresenter
import com.emmaguy.audiocastradio.features.DividerItemDecoration
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.jakewharton.rxrelay.PublishRelay
import kotlinx.android.synthetic.main.activity_audio_streams.*
import rx.Observable

class AudioStreamListActivity(val presenter: AudioStreamListPresenter = AudioStreamListModule.audioStreamsPresenter,
                              val castManager: VideoCastManager = AudioStreamListModule.castManager) :
        AbstractActivity<AudioStreamListPresenter.View>(), AudioStreamListPresenter.View {
    private val onAudioStreamClickedRelay: PublishRelay<AudioStream> = PublishRelay.create()

    override fun getLayoutId(): Int {
        return R.layout.activity_audio_streams
    }

    override fun setAudioStreams(audioStreams: List<AudioStream>) {
        audioStreamsRecyclerView.setHasFixedSize(true)
        audioStreamsRecyclerView.adapter = AudioStreamListAdapter(audioStreams, onAudioStreamClickedRelay)
        audioStreamsRecyclerView.itemAnimator = DefaultItemAnimator()
        audioStreamsRecyclerView.layoutManager = LinearLayoutManager(this)
        audioStreamsRecyclerView.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.divider_audio_streams)));
    }

    override fun startStream(audioStream: AudioStream) {
        castManager.loadMedia(audioStream.toMediaInfo(), true, 0)
    }

    override fun onAudioStreamClicked(): Observable<AudioStream> {
        return onAudioStreamClickedRelay
    }

    override fun getPresenter(): AbstractPresenter<AudioStreamListPresenter.View> {
        return presenter
    }

    override fun getPresenterView(): AudioStreamListPresenter.View {
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
}
