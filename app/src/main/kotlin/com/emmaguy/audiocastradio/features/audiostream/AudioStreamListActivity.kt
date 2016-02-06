package com.emmaguy.audiocastradio.features.audiostream

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import com.emmaguy.audiocastradio.App
import com.emmaguy.audiocastradio.R
import com.emmaguy.audiocastradio.base.AbstractActivity
import com.emmaguy.audiocastradio.base.AbstractPresenter
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.jakewharton.rxrelay.PublishRelay
import kotlinx.android.synthetic.main.activity_audio_streams.*
import rx.Observable

class AudioStreamListActivity(val module: AudioStreamListModule = AudioStreamListModule(App.instance.appModule),
                              val presenter: AudioStreamListPresenter = module.audioStreamsPresenter,
                              val castManager: VideoCastManager = module.appModule.castManager,
                              val res: Resources = module.appModule.resources) :
        AbstractActivity<AudioStreamListPresenter.View>(), AudioStreamListPresenter.View {
    private val onAudioStreamClickedRelay: PublishRelay<AudioStream> = PublishRelay.create()

    override fun getLayoutId(): Int {
        return R.layout.activity_audio_streams
    }

    override fun setAudioStreams(audioStreams: List<AudioStream>) {
        audioStreamsRecyclerView.setHasFixedSize(true)
        audioStreamsRecyclerView.adapter = AudioStreamListAdapter(audioStreams, onAudioStreamClickedRelay, res)
        audioStreamsRecyclerView.itemAnimator = DefaultItemAnimator()
        audioStreamsRecyclerView.layoutManager = GridLayoutManager(this, 3)
        audioStreamsRecyclerView.addItemDecoration(MarginDecoration(res.getDimensionPixelSize(R.dimen.item_audio_stream_list_margin)))
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
