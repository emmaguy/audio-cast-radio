package com.emmaguy.audiocastradio.feature

import com.emmaguy.audiocastradio.data.AudioStream


interface AnalyticsService {
    fun logStartCastingEvent(audioStream: AudioStream)
}