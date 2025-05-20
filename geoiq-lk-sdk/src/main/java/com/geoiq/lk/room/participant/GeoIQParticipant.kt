package com.geoiq.lk.room.participant

import io.livekit.android.room.participant.Participant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.geoiq.lk.track.GeoIQTrack

/**
 * Represents a participant in a GeoIQ room.
 */
open class GeoIQParticipant internal constructor(protected val participant: Participant) {
    
    /**
     * The participant's identity.
     */
    val identity: String
        get() = participant.identity
    
    /**
     * The participant's name.
     */
    val name: String?
        get() = participant.name
    
    /**
     * Whether the participant is speaking.
     */
    val isSpeaking: Boolean
        get() = participant.isSpeaking
    
    /**
     * Flow of the participant's tracks.
     */
    val tracks: Flow<List<GeoIQTrack>>
        get() = participant.tracks.map { tracks ->
            tracks.values.map { GeoIQTrack.fromLiveKitTrack(it.track) }
        }
}
