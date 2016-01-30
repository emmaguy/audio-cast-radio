package com.emmaguy.audiocastradio.features.audiostream

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.emmaguy.audiocastradio.R
import com.jakewharton.rxrelay.PublishRelay

internal class AudioStreamListAdapter(val audioStreams: List<AudioStream>, val onAudioStreamClickedRelay: PublishRelay<AudioStream>) : RecyclerView.Adapter<AudioStreamListAdapter.AudioStreamViewHolder>() {
    override fun onBindViewHolder(holder: AudioStreamViewHolder, position: Int) {
        holder.setAudioStream(audioStreams[position])
    }

    override fun getItemCount(): Int {
        return audioStreams.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioStreamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_audio_stream, parent, false);
        return AudioStreamViewHolder(view, audioStreams, onAudioStreamClickedRelay);
    }

    class AudioStreamViewHolder(itemView: View,
                                val audioStreams: List<AudioStream>,
                                val onAudioStreamClickedRelay: PublishRelay<AudioStream>) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.audioStreamTitle) as TextView

        init {
            val viewGroup = itemView.findViewById(R.id.audioStreamsItemContainer) as ViewGroup
            viewGroup.setOnClickListener { onAudioStreamClickedRelay.call(audioStreams[adapterPosition]) }
        }

        fun setAudioStream(audioStream: AudioStream) {
            titleTextView.text = audioStream.title;
        }
    }
}