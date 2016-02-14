package com.emmaguy.audiocastradio

import android.content.Context
import android.content.res.Resources
import android.view.Menu
import com.emmaguy.audiocastradio.feature.CastManager
import com.emmaguy.audiocastradio.data.CastState
import com.emmaguy.audiocastradio.data.MediaState
import com.emmaguy.audiocastradio.data.AudioStream
import com.emmaguy.audiocastradio.feature.audiostream.AudioStreamListActivity
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.gms.cast.MediaStatus
import com.google.android.libraries.cast.companionlibrary.cast.CastConfiguration
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl
import com.jakewharton.rxrelay.BehaviorRelay
import org.json.JSONObject
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class AppModule(val app: App) {
    val onCastStateChanged: BehaviorRelay<CastState> = BehaviorRelay.create()
    val resources = app.resources
    val uiScheduler = AndroidSchedulers.mainThread()

    private val videoCastManager = castManager(app, resources)
    val castManager: CastManager = object : CastManager {
        override fun incrementUiCounter() {
            videoCastManager.incrementUiCounter()
        }

        override fun decrementUiCounter() {
            videoCastManager.decrementUiCounter()
        }

        override fun addMediaRouterButton(menu: Menu, menuItemId: Int) {
            videoCastManager.addMediaRouterButton(menu, menuItemId)
        }

        override fun play() {
            videoCastManager.play()
        }

        override fun loadStream(audioStream: AudioStream) {
            videoCastManager.loadMedia(audioStream.toMediaInfo(), true, 0)
        }

        override fun pause() {
            videoCastManager.pause()
        }
    }

    private fun castManager(context: Context, resources: Resources): VideoCastManager {
        val options = CastConfiguration.Builder(resources.getString(R.string.cast_app_id))
                .enableAutoReconnect()
                .enableWifiReconnection()
                .enableDebug()
                .enableLockScreen()
                .setLaunchOptions(false, Locale.getDefault())
                .setNextPrevVisibilityPolicy(CastConfiguration.NEXT_PREV_VISIBILITY_POLICY_HIDDEN)
                .setTargetActivity(AudioStreamListActivity::class.java) // currently singleInstance
                .enableNotification()
                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_PLAY_PAUSE, true)
                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_DISCONNECT, true)
                .build()

        val castManager = VideoCastManager.initialize(context, options)
        initialiseCast(castManager)

        return castManager
    }

    private fun initialiseCast(castManager: VideoCastManager) {
        castManager.addVideoCastConsumer(object : VideoCastConsumerImpl() {
            override fun onApplicationConnected(appMetadata: ApplicationMetadata, sessionId: String, wasLaunched: Boolean) {
                super.onApplicationConnected(appMetadata, sessionId, wasLaunched)

                if (appMetadata.isNamespaceSupported(NAMESPACE_MEDIA_PLAYBACK)) {
                    onCastStateChanged.call(buildCastState())
                }
            }

            override fun onRemoteMediaPlayerStatusUpdated() {
                super.onRemoteMediaPlayerStatusUpdated()

                onCastStateChanged.call(buildCastState())
            }

            override fun onDisconnected() {
                super.onDisconnected()

                onCastStateChanged.call(buildCastState())
            }

            private fun buildCastState(): CastState {
                val mediaStatus: MediaStatus?
                val contentId: String?
                val customData: JSONObject?
                if (castManager.isConnected) {
                    mediaStatus = castManager.remoteMediaPlayer?.mediaStatus
                    contentId = castManager.remoteMediaInformation?.contentId
                    customData = castManager.remoteMediaInformation?.customData
                } else {
                    mediaStatus = null
                    contentId = null
                    customData = null
                }

                val mediaState: MediaState
                if (mediaStatus == null) {
                    mediaState = MediaState.IDLE
                } else {
                    if (mediaStatus.playerState == MediaStatus.PLAYER_STATE_BUFFERING) {
                        mediaState = MediaState.BUFFERING
                    } else if (mediaStatus.playerState == MediaStatus.PLAYER_STATE_PLAYING) {
                        mediaState = MediaState.PLAYING
                    } else if (mediaStatus.playerState == MediaStatus.PLAYER_STATE_PAUSED) {
                        mediaState = MediaState.PAUSED
                    } else {
                        mediaState = MediaState.UNKNOWN
                    }
                }

                return CastState(castManager.isConnected,
                        mediaState,
                        contentId,
                        customData)
            }
        })
    }

    companion object {
        private val NAMESPACE_MEDIA_PLAYBACK = "urn:x-cast:com.google.cast.media"
    }
}