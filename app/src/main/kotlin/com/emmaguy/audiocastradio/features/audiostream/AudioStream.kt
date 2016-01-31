package com.emmaguy.audiocastradio.features.audiostream

import android.net.Uri
import com.emmaguy.audiocastradio.BuildConfig
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.common.images.WebImage
import org.json.JSONObject

data class AudioStream(val title: String, val streamUrl: String, val imageUrl: String) {
    fun toMediaInfo(): MediaInfo {
        val mediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, title)
        mediaMetadata.addImage(WebImage(Uri.parse(imageUrl)))

        return MediaInfo.Builder(streamUrl)
                .setContentType("audio/mpeg")
                .setStreamType(MediaInfo.STREAM_TYPE_LIVE)
                .setMetadata(mediaMetadata)
                .setCustomData(JSONObject("{$KEY_APP_NAME:\"$VALUE_APP_NAME\"}"))
                .build()
    }

    companion object {
        fun isFromApp(mediaInformation: MediaInfo?): Boolean {
            return mediaInformation != null
                    && mediaInformation.customData.has(KEY_APP_NAME)
                    && mediaInformation.customData.getString(KEY_APP_NAME).equals(VALUE_APP_NAME)
        }

        private val KEY_APP_NAME = "app_name"
        private val VALUE_APP_NAME = BuildConfig.APPLICATION_ID
    }
}