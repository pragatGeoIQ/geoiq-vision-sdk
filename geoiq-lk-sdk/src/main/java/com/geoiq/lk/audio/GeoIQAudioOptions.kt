package com.geoiq.lk.audio

import io.livekit.android.audio.AudioOptions as LiveKitAudioOptions
import io.livekit.android.audio.AudioType as LiveKitAudioType

/**
 * Audio mode for the GeoIQ room.
 */
enum class GeoIQAudioMode {
    /**
     * Optimized for voice communication.
     */
    COMMUNICATION,
    
    /**
     * Optimized for media playback.
     */
    MEDIA
}

/**
 * Audio options for the GeoIQ room.
 */
data class GeoIQAudioOptions(
    /**
     * The audio mode to use.
     */
    val audioMode: GeoIQAudioMode = GeoIQAudioMode.COMMUNICATION
) {
    /**
     * Convert to LiveKit audio options.
     * This method is internal to the SDK and not exposed in public documentation.
     */
    internal fun toLiveKitAudioOptions(): LiveKitAudioOptions {
        val audioType = when (audioMode) {
            GeoIQAudioMode.COMMUNICATION -> LiveKitAudioType.VoiceAudioType()
            GeoIQAudioMode.MEDIA -> LiveKitAudioType.MediaAudioType()
        }
        
        return LiveKitAudioOptions(
            audioOutputType = audioType
        )
    }
}
