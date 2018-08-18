package com.emmaguy.castradio.feature

import com.emmaguy.castradio.data.AudioStream


interface AnalyticsService {
    fun logStartCastingEvent(audioStream: AudioStream)
}