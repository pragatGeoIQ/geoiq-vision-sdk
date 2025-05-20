package com.geoiq.lk

import android.content.Context
import io.livekit.android.LiveKit
import io.livekit.android.room.Room
import com.geoiq.lk.audio.GeoIQAudioOptions

/**
 * Main entry point for the GeoIQ-LK SDK.
 */
class GeoIQLK {
    companion object {
        /**
         * Creates a new GeoIQ Room instance.
         *
         * @param context Android application context
         * @return A new GeoIQRoom instance
         */
        fun create(context: Context): GeoIQRoom {
            val livekitRoom = LiveKit.create(context)
            return GeoIQRoom(livekitRoom)
        }
        
        /**
         * Creates a new GeoIQ Room instance with custom audio options.
         *
         * @param context Android application context
         * @param audioOptions Custom audio configuration options
         * @return A new GeoIQRoom instance
         */
        fun create(context: Context, audioOptions: GeoIQAudioOptions): GeoIQRoom {
            val livekitAudioOptions = audioOptions.toLiveKitAudioOptions()
            val livekitRoom = LiveKit.create(
                appContext = context,
                overrides = io.livekit.android.LiveKitOverrides(
                    audioOptions = livekitAudioOptions
                )
            )
            return GeoIQRoom(livekitRoom)
        }
    }
}
