package com.emmaguy.audiocastradio.api

import com.emmaguy.audiocastradio.data.AudioStream
import io.reactivex.Observable
import retrofit2.http.GET


interface AudioStreamsApi {
    @GET("radio-stations.json")
    fun audioStreams(): Observable<List<AudioStream>>
}