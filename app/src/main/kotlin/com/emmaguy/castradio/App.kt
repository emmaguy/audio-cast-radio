package com.emmaguy.castradio

import android.app.Application
import com.emmaguy.castradio.di.Inject
import com.emmaguy.castradio.di.InjectorImpl
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Inject.instance = InjectorImpl(AppComponent(this))
    }
}
