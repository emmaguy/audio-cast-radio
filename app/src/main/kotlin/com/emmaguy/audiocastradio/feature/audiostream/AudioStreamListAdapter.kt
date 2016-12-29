package com.emmaguy.audiocastradio.feature.audiostream

import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.emmaguy.audiocastradio.R
import com.emmaguy.audiocastradio.data.AudioStream
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class AudioStreamListAdapter(val audioStreams: List<AudioStream>,
                             val onAudioStreamClickedSubject: BehaviorSubject<AudioStream>,
                             val resources: Resources)
    : RecyclerView.Adapter<AudioStreamListAdapter.AudioStreamViewHolder>() {
    private val assignedColours = ArrayList<Int>()

    init {
        val colours = resources.obtainTypedArray(R.array.material_design_colours)

        // purposefully seed with a consistent number so the colours are the same across app restarts
        val random = Random(Int.MIN_VALUE.toLong())
        while (assignedColours.size < audioStreams.size) {
            val index = (random.nextInt(colours.length()))
            val randomColour = colours.getColor(index, Color.BLACK)
            if (!assignedColours.contains(randomColour)) {
                assignedColours.add(randomColour)
            }
        }

        colours.recycle()
    }

    override fun onBindViewHolder(holder: AudioStreamViewHolder, position: Int) {
        holder.setAudioStream(audioStreams[position])
    }

    override fun getItemCount(): Int {
        return audioStreams.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioStreamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_audio_stream, parent, false)
        return AudioStreamViewHolder(view, audioStreams, onAudioStreamClickedSubject, assignedColours)
    }

    class AudioStreamViewHolder(itemView: View,
                                val audioStreams: List<AudioStream>,
                                val onAudioStreamClickedSubject: BehaviorSubject<AudioStream>,
                                val assignedColours: ArrayList<Int>) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.audioStreamTitle) as TextView

        init {
            itemView.setOnClickListener { onAudioStreamClickedSubject.onNext(audioStreams[adapterPosition]) }
        }

        fun setAudioStream(audioStream: AudioStream) {
            titleTextView.text = audioStream.name
            itemView.setBackgroundColor(assignedColours.elementAt(adapterPosition))
        }
    }
}