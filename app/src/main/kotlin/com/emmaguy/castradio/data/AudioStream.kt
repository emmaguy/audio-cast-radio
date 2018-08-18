package com.emmaguy.castradio.data

import android.net.Uri
import com.emmaguy.castradio.BuildConfig
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.common.images.WebImage
import org.json.JSONObject

data class AudioStream(val name: String, val streamUrl: String, var imageUrl: String) {
    fun toMediaInfo(): MediaInfo {
        val mediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, name)
        mediaMetadata.addImage(WebImage(Uri.parse(imageUrl)))

        return MediaInfo.Builder(streamUrl)
                .setContentType("audio/mpeg")
                .setStreamType(MediaInfo.STREAM_TYPE_LIVE)
                .setMetadata(mediaMetadata)
                .setCustomData(JSONObject("{$KEY_APP_NAME:\"$VALUE_APP_NAME\"}"))
                .build()
    }

    companion object {
        private val KEY_APP_NAME = "app_name"
        private val VALUE_APP_NAME = BuildConfig.APPLICATION_ID
    }
}