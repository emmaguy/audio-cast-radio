package com.emmaguy.audiocastradio.data

import com.emmaguy.audiocastradio.data.MediaState
import org.json.JSONObject

data class CastState(val isConnected: Boolean, val mediaState: MediaState,
                     val contentId: String?, val customData: JSONObject?) {
}