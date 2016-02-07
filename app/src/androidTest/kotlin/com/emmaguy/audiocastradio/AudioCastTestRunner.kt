package com.emmaguy.audiocastradio

import android.app.KeyguardManager
import android.content.Context
import android.os.PowerManager
import android.support.test.runner.AndroidJUnitRunner

class AudioCastTestRunner : AndroidJUnitRunner() {
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onStart() {
        val app = targetContext.applicationContext

        val name = AudioCastTestRunner::class.java.simpleName

        // Unlock the device so that the tests can input keystrokes.
        val keyguard = app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguard.newKeyguardLock(name).disableKeyguard()

        // Wake up the screen
        val power = app.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = power.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, name)
        wakeLock!!.acquire()

        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()

        wakeLock!!.release()
    }
}