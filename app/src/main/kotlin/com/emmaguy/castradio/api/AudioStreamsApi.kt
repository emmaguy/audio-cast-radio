package com.emmaguy.castradio.api

import com.emmaguy.castradio.data.AudioStream
import io.reactivex.Observable
import retrofit2.http.GET


interface AudioStreamsApi {
    @GET("radio-stations.json")
    fun audioStreams(): Observable<List<AudioStream>>
}