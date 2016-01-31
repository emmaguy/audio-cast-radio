package com.emmaguy.audiocastradio

import com.jakewharton.rxrelay.BehaviorRelay

public class AppModule {
    companion object {
        val onCastCapabilityInitialised: BehaviorRelay<Unit> = BehaviorRelay.create()
    }
}