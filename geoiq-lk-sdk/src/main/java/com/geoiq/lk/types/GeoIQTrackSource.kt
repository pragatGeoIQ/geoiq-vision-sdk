package com.geoiq.lk.types

import io.livekit.android.room.track.Track.Source as LiveKitTrackSource

/**
 * Source of a track.
 */
enum class GeoIQTrackSource {
    UNKNOWN,
    CAMERA,
    MICROPHONE,
    SCREEN_SHARE;
    
    internal companion object {
        fun fromLiveKitSource(source: LiveKitTrackSource): GeoIQTrackSource {
            return when (source) {
                LiveKitTrackSource.UNKNOWN -> UNKNOWN
                LiveKitTrackSource.CAMERA -> CAMERA
                LiveKitTrackSource.MICROPHONE -> MICROPHONE
                LiveKitTrackSource.SCREEN_SHARE -> SCREEN_SHARE
                else -> UNKNOWN
            }
        }
        
        fun toLiveKitSource(source: GeoIQTrackSource): LiveKitTrackSource {
            return when (source) {
                UNKNOWN -> LiveKitTrackSource.UNKNOWN
                CAMERA -> LiveKitTrackSource.CAMERA
                MICROPHONE -> LiveKitTrackSource.MICROPHONE
                SCREEN_SHARE -> LiveKitTrackSource.SCREEN_SHARE
            }
        }
    }
}
