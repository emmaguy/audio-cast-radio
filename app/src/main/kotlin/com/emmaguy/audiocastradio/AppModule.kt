package com.emmaguy.audiocastradio

import android.content.Context
import android.content.res.Resources
import com.emmaguy.audiocastradio.features.audiostream.AudioStreamListActivity
import com.google.android.libraries.cast.companionlibrary.cast.CastConfiguration
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.jakewharton.rxrelay.BehaviorRelay
import java.util.*

class AppModule(val app: App) {
    val onCastCapabilityInitialised: BehaviorRelay<Unit> = BehaviorRelay.create()

    val resources = app.resources
    val castManager = castManager(app, resources)

    private fun castManager(context: Context, resources: Resources): VideoCastManager {
        val options = CastConfiguration.Builder(resources.getString(R.string.cast_app_id))
                .enableAutoReconnect()
                .enableWifiReconnection()
                .enableDebug()
                .enableLockScreen()
                .setLaunchOptions(false, Locale.getDefault())
                .setNextPrevVisibilityPolicy(CastConfiguration.NEXT_PREV_VISIBILITY_POLICY_HIDDEN)
                .setTargetActivity(AudioStreamListActivity::class.java) // currently singleInstance
                .enableNotification()
                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_PLAY_PAUSE, true)
                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_DISCONNECT, true)
                .build()

        return VideoCastManager.initialize(context, options)
    }
}