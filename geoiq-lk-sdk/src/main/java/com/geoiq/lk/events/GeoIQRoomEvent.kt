package com.geoiq.lk.events

import com.geoiq.lk.room.participant.GeoIQParticipant
import com.geoiq.lk.track.GeoIQTrack

/**
 * Events that can occur in a GeoIQ room.
 */
sealed class GeoIQRoomEvent {
    /**
     * The connection to the room has been established.
     */
    object Connected : GeoIQRoomEvent()
    
    /**
     * The connection to the room has been closed.
     */
    object Disconnected : GeoIQRoomEvent()
    
    /**
     * A participant has connected to the room.
     */
    data class ParticipantConnected(val participant: GeoIQParticipant) : GeoIQRoomEvent()
    
    /**
     * A participant has disconnected from the room.
     */
    data class ParticipantDisconnected(val participant: GeoIQParticipant) : GeoIQRoomEvent()
    
    /**
     * A track has been subscribed to.
     */
    data class TrackSubscribed(
        val track: GeoIQTrack,
        val participant: GeoIQParticipant
    ) : GeoIQRoomEvent()
    
    /**
     * A track has been unsubscribed from.
     */
    data class TrackUnsubscribed(
        val track: GeoIQTrack,
        val participant: GeoIQParticipant
    ) : GeoIQRoomEvent()
}
