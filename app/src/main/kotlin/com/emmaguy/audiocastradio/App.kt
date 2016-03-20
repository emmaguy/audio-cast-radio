package com.emmaguy.audiocastradio

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.gms.cast.MediaStatus
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl
import io.fabric.sdk.android.Fabric
import org.json.JSONObject
import timber.log.Timber

class App() : Application() {
    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics());
        Crashlytics.setString("Git SHA", BuildConfig.GIT_SHA);

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instantiateInjector()
    }

    fun instantiateInjector() {
        Inject.instance = InjectorImpl(AppComponentImpl(this))
    }
}
