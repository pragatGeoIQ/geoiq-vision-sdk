package com.geoiq.lk.room.participant

import android.content.Intent
import io.livekit.android.room.participant.LocalParticipant

/**
 * Represents the local participant in a GeoIQ room.
 */
class GeoIQLocalParticipant internal constructor(
    private val localParticipant: LocalParticipant
) : GeoIQParticipant(localParticipant) {
    
    /**
     * Enable or disable the camera.
     *
     * @param enabled Whether the camera should be enabled
     */
    suspend fun setCameraEnabled(enabled: Boolean) {
        localParticipant.setCameraEnabled(enabled)
    }
    
    /**
     * Enable or disable the microphone.
     *
     * @param enabled Whether the microphone should be enabled
     */
    suspend fun setMicrophoneEnabled(enabled: Boolean) {
        localParticipant.setMicrophoneEnabled(enabled)
    }
    
    /**
     * Enable or disable screen sharing.
     *
     * @param enabled Whether screen sharing should be enabled
     * @param mediaProjectionPermissionResultData The result data from the screen capture intent
     */
    suspend fun setScreenShareEnabled(enabled: Boolean, mediaProjectionPermissionResultData: Intent? = null) {
        if (enabled && mediaProjectionPermissionResultData != null) {
            localParticipant.setScreenShareEnabled(true, mediaProjectionPermissionResultData)
        } else if (!enabled) {
            localParticipant.setScreenShareEnabled(false)
        }
    }
}
