package com.emmaguy.audiocastradio.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class AudioStreamJsonAdapter {
    @FromJson fun audioStreamFromJson(audioStreamJson: AudioStreamJson): AudioStream {
        return AudioStream(audioStreamJson.name, audioStreamJson.stream_url, audioStreamJson.image_url)
    }

    @ToJson fun audioStreamToJson(audioStream: AudioStream): AudioStreamJson {
        return AudioStreamJson(audioStream.name, audioStream.streamUrl, audioStream.imageUrl)
    }
}