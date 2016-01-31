package com.emmaguy.audiocastradio

import android.app.Application
import com.emmaguy.audiocastradio.features.audiostream.AudioStream
import com.emmaguy.audiocastradio.features.audiostream.AudioStreamListActivity
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.libraries.cast.companionlibrary.cast.CastConfiguration
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl
import com.jakewharton.rxrelay.BehaviorRelay
import java.util.*

class App(val onCastCapabilityInitialised: BehaviorRelay<Unit> = AppModule.onCastCapabilityInitialised) : Application() {
    override fun onCreate() {
        super.onCreate()

        val options = CastConfiguration.Builder(getString(R.string.cast_app_id))
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

        val castManager = VideoCastManager.initialize(this, options)
        castManager.addVideoCastConsumer(object : VideoCastConsumerImpl() {
            override fun onApplicationConnected(appMetadata: ApplicationMetadata, sessionId: String, wasLaunched: Boolean) {
                super.onApplicationConnected(appMetadata, sessionId, wasLaunched)

                if (appMetadata.isNamespaceSupported(NAMESPACE_MEDIA_PLAYBACK)) {
                    onCastCapabilityInitialised.call(Unit)
                }
            }

            override fun onRemoteMediaPlayerStatusUpdated() {
                super.onRemoteMediaPlayerStatusUpdated()

                if (castManager.isConnected
                        && castManager.isRemoteMediaPlaying
                        && AudioStream.isFromApp(castManager.remoteMediaInformation)) {
                    onCastCapabilityInitialised.call(Unit)
                }
            }
        })
    }

    companion object {
        private val NAMESPACE_MEDIA_PLAYBACK = "urn:x-cast:com.google.cast.media"
    }
}