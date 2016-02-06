package com.emmaguy.audiocastradio.features.audiostream

import com.emmaguy.audiocastradio.AppModule

class AudioStreamListModule(val appModule: AppModule) {
    internal val audioStreamsPresenter = AudioStreamListPresenter(audioStreams(), appModule.onCastCapabilityInitialised)

    private fun audioStreams(): List<AudioStream> {
        return listOf(
                AudioStream("Klara",
                        "http://mp3.streampower.be/klara-high.mp3",
                        "http://www.radioviainternet.be/images/logos/logo-klara.png"),
                AudioStream("Nostalgie",
                        "http://nostalgiewhatafeeling.ice.infomaniak.ch/nostalgiewhatafeeling-128.mp3",
                        "http://www.nostalgie.be/radioplayer/img/radios/premium.png"),
                AudioStream("Radio 1",
                        "http://mp3.streampower.be/radio1-high.mp3",
                        "http://www.radio1.be/sites/all/themes/benny/logo.png"),
                AudioStream("Studio Brussel",
                        "http://mp3.streampower.be/stubru-high.mp3",
                        "http://cds.stubru.be/sites/all/themes/custom/netsites_admin/images/logos/logo.png"))
    }
}