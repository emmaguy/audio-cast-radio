package com.emmaguy.audiocastradio.feature.audiostream

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.View
import com.emmaguy.audiocastradio.AppComponent
import com.emmaguy.audiocastradio.Inject
import com.emmaguy.audiocastradio.R
import com.emmaguy.audiocastradio.base.AbstractActivity
import com.emmaguy.audiocastradio.base.AbstractPresenter
import com.emmaguy.audiocastradio.common.widget.MarginDecoration
import com.emmaguy.audiocastradio.data.AudioStream
import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.PublishRelay
import kotlinx.android.synthetic.main.activity_audio_streams.*
import rx.Observable

class AudioStreamListActivity() : AbstractActivity<AudioStreamListPresenter.View>(),
        AudioStreamListPresenter.View, AppComponent by Inject.instance {
    private val onPlayPauseAudioStreamClickedRelay: PublishRelay<Unit> = PublishRelay.create()
    private val presenter = AudioStreamListPresenter(uiScheduler, audioStreams, onCastStateChanged, castManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        audioStreamListTogglePlayPauseButton.setOnClickListener { onPlayPauseAudioStreamClickedRelay.call(Unit) }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_audio_streams
    }

    override fun setAudioStreams(audioStreams: List<AudioStream>) {
        audioStreamListRecyclerView.setHasFixedSize(true)
        audioStreamListRecyclerView.adapter = AudioStreamListAdapter(audioStreams, onAudioStreamClickedRelay, res)
        audioStreamListRecyclerView.itemAnimator = DefaultItemAnimator()
        audioStreamListRecyclerView.layoutManager = GridLayoutManager(this, res.getInteger(R.integer.audio_stream_list_columns))
        audioStreamListRecyclerView.addItemDecoration(MarginDecoration(res.getDimensionPixelSize(R.dimen.item_audio_stream_list_margin)))
    }

    override fun onAudioStreamClicked(): Observable<AudioStream> {
        return onAudioStreamClickedRelay
    }

    override fun onTogglePlayPauseAudioStreamClicked(): Observable<Unit> {
        return onPlayPauseAudioStreamClickedRelay
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

    override fun showLoadingView() {
        audioStreamListProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoadingView() {
        audioStreamListProgressBar.visibility = View.GONE
    }

    override fun showPauseStreamView() {
        audioStreamListTogglePlayPauseButton.visibility = View.VISIBLE
        audioStreamListTogglePlayPauseButton.setImageResource(android.R.drawable.ic_media_pause)
    }

    override fun showPlayStreamView() {
        audioStreamListTogglePlayPauseButton.visibility = View.VISIBLE
        audioStreamListTogglePlayPauseButton.setImageResource(android.R.drawable.ic_media_play)
    }

    override fun hidePlayPauseStreamView() {
        audioStreamListTogglePlayPauseButton.visibility = View.GONE
    }

    companion object {
        private val onAudioStreamClickedRelay: BehaviorRelay<AudioStream> = BehaviorRelay.create()
    }
}
