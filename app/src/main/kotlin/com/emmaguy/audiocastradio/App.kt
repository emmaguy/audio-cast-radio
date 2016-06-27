package com.emmaguy.audiocastradio

import android.app.Application
import com.emmaguy.audiocastradio.di.Inject
import com.emmaguy.audiocastradio.di.InjectorImpl
import timber.log.Timber

class App() : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Inject.instance = InjectorImpl(AppComponent(this))
    }
}
