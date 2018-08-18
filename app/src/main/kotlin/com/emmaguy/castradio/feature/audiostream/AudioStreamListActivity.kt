package com.emmaguy.castradio.feature.audiostream

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.emmaguy.castradio.R
import com.emmaguy.castradio.base.BaseActivity
import com.emmaguy.castradio.base.BaseComponent
import com.emmaguy.castradio.base.BasePresenter
import com.emmaguy.castradio.common.widget.MarginDecoration
import com.emmaguy.castradio.data.AudioStream
import com.emmaguy.castradio.di.Inject
import com.google.android.gms.cast.framework.CastButtonFactory
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_audio_streams.*

class AudioStreamListActivity : BaseActivity<AudioStreamListPresenter.View>(),
        AudioStreamListPresenter.View, BaseComponent by Inject.instance {
    private val presenter = AudioStreamListPresenter(uiScheduler, ioScheduler, audioStreamApi, onCastStateChanged, castManager, analyticsService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        castManager.init(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_audio_streams
    }

    override fun setAudioStreams(audioStreams: List<AudioStream>) {
        audioStreamListRecyclerView.setHasFixedSize(true)
        audioStreamListRecyclerView.adapter = AudioStreamListAdapter(audioStreams, audioStreamClickedSubject, res)
        audioStreamListRecyclerView.itemAnimator = DefaultItemAnimator()
        audioStreamListRecyclerView.layoutManager = GridLayoutManager(this, res.getInteger(R.integer.audio_stream_list_columns))
        audioStreamListRecyclerView.addItemDecoration(MarginDecoration(res.getDimensionPixelSize(R.dimen.item_audio_stream_list_margin)))
    }

    override fun onAudioStreamClicked(): Observable<AudioStream> {
        return audioStreamClickedSubject
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
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.cast_audio_menu_item)
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

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    companion object {
        private val audioStreamClickedSubject: BehaviorSubject<AudioStream> = BehaviorSubject.create()
    }
}
