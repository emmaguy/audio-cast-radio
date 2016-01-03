package com.emmaguy.audiocastradio

import android.app.Application
import com.emmaguy.audiocastradio.audiostreams.AudioStreamsActivity
import com.google.android.libraries.cast.companionlibrary.cast.CastConfiguration
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val options = CastConfiguration.Builder(getString(R.string.cast_app_id))
                .enableAutoReconnect()
                .enableWifiReconnection()
                .enableDebug()
                .enableLockScreen()
                .setLaunchOptions(false, Locale.getDefault())
                .setNextPrevVisibilityPolicy(CastConfiguration.NEXT_PREV_VISIBILITY_POLICY_HIDDEN)
                .setTargetActivity(AudioStreamsActivity::class.java)
                .enableNotification()
                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_PLAY_PAUSE, true)
                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_DISCONNECT, true)
                .build()
        VideoCastManager.initialize(this, options)
    }
}