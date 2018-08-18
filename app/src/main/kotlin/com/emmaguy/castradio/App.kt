package com.emmaguy.castradio

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.emmaguy.castradio.di.Inject
import com.emmaguy.castradio.di.InjectorImpl
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Fabric.with(this, Crashlytics())
        Inject.instance = InjectorImpl(AppComponent(this))
    }
}
