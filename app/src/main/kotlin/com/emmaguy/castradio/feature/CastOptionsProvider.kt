package com.emmaguy.castradio.feature

import android.content.Context
import com.emmaguy.castradio.R
import com.emmaguy.castradio.feature.audiostream.AudioStreamListActivity
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.framework.media.NotificationOptions

class CastOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {
        val notificationOptions = NotificationOptions.Builder()
                .setTargetActivityClassName(AudioStreamListActivity::class.java.name)
                .build()
        val mediaOptions = CastMediaOptions.Builder().setNotificationOptions(notificationOptions).build()

        return CastOptions.Builder().setReceiverApplicationId(context.getString(R.string.cast_app_id))
                .setCastMediaOptions(mediaOptions)
                .build()
    }

    override fun getAdditionalSessionProviders(context: Context?): MutableList<SessionProvider>? {
        return null
    }
}