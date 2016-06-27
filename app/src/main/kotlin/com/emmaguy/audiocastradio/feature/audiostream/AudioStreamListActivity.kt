package com.emmaguy.audiocastradio.feature.audiostream

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import com.emmaguy.audiocastradio.R
import com.emmaguy.audiocastradio.base.BaseActivity
import com.emmaguy.audiocastradio.base.BaseComponent
import com.emmaguy.audiocastradio.base.BasePresenter
import com.emmaguy.audiocastradio.common.widget.MarginDecoration
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.di.Inject
import com.jakewharton.rxrelay.BehaviorRelay
import kotlinx.android.synthetic.main.activity_audio_streams.*
import rx.Observable

class AudioStreamListActivity() : BaseActivity<AudioStreamListPresenter.View>(),
        AudioStreamListPresenter.View, BaseComponent by Inject.instance {
    private val presenter = AudioStreamListPresenter(uiScheduler, audioStreams, onCastStateChanged, castManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        castManager.init(this)
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

    override fun getPresenter(): BasePresenter<AudioStreamListPresenter.View> {
        return presenter
    }

    override fun getPresenterView(): AudioStreamListPresenter.View {
        return this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.cast_audio, menu)
        castManager.setUpMediaRouteButton(menu, R.id.cast_audio_menu_item)
        return true
    }

    override fun onResume() {
        super.onResume()
        castManager.onResume()
    }

    override fun onPause() {
        castManager.onPause()
        super.onPause()
    }

    companion object {
        private val onAudioStreamClickedRelay: BehaviorRelay<AudioStream> = BehaviorRelay.create()
    }
}
