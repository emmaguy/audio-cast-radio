package com.emmaguy.audiocastradio

import org.json.JSONObject

data class CastState(val isConnected: Boolean, val isRemoteMediaPlaying: Boolean, val customData: JSONObject?) {
}