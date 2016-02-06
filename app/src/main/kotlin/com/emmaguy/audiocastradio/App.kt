package com.emmaguy.audiocastradio

import android.app.Application
import com.emmaguy.audiocastradio.features.audiostream.AudioStream
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl
import timber.log.Timber

class App() : Application() {
    private val onCastCapabilityInitialised by lazy { appModule.onCastCapabilityInitialised }

    val appModule by lazy { AppModule(this) }

    override fun onCreate() {
        super.onCreate()

        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initialiseCast(appModule.castManager)
    }

    private fun initialiseCast(castManager: VideoCastManager) {
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
        lateinit var instance: App
        private val NAMESPACE_MEDIA_PLAYBACK = "urn:x-cast:com.google.cast.media"
    }
}
