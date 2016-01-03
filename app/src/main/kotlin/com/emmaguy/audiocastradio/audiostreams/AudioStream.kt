package com.emmaguy.audiocastradio.audiostreams

import android.net.Uri
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.common.images.WebImage

data class AudioStream(val title: String, val streamUrl: String, val imageUrl: String) {
    fun toMediaInfo(): MediaInfo {
        val mediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, title)
        mediaMetadata.addImage(WebImage(Uri.parse(imageUrl)))

        return MediaInfo.Builder(streamUrl)
                .setContentType("audio/mpeg")
                .setStreamType(MediaInfo.STREAM_TYPE_LIVE)
                .setMetadata(mediaMetadata)
                .build()
    }
}