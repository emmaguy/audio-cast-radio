package com.emmaguy.audiocastradio

import android.app.Application
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.gms.cast.MediaStatus
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl
import org.json.JSONObject
import timber.log.Timber

class App() : Application() {
    val appModule by lazy { AppModule(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        lateinit var instance: App
    }
}
