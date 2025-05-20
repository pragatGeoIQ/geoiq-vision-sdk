package com.geoiq.lk.events.internal

import io.livekit.android.events.RoomEvent
import com.geoiq.lk.events.GeoIQRoomEvent
import com.geoiq.lk.room.participant.GeoIQParticipant
import com.geoiq.lk.track.GeoIQTrack

/**
 * Internal utilities for converting between LiveKit and GeoIQ-LK event types.
 */
internal object EventConverter {
    /**
     * Convert a LiveKit RoomEvent to a GeoIQRoomEvent.
     * Returns null for events that should not be exposed.
     */
    fun convertEvent(event: RoomEvent): GeoIQRoomEvent? {
        return when (event) {
            is RoomEvent.Connected -> GeoIQRoomEvent.Connected
            is RoomEvent.Disconnected -> GeoIQRoomEvent.Disconnected
            is RoomEvent.ParticipantConnected -> 
                GeoIQRoomEvent.ParticipantConnected(GeoIQParticipant(event.participant))
            is RoomEvent.ParticipantDisconnected -> 
                GeoIQRoomEvent.ParticipantDisconnected(GeoIQParticipant(event.participant))
            is RoomEvent.TrackSubscribed -> {
                val geoiqTrack = GeoIQTrack.fromLiveKitTrack(event.track)
                GeoIQRoomEvent.TrackSubscribed(geoiqTrack, GeoIQParticipant(event.participant))
            }
            is RoomEvent.TrackUnsubscribed -> {
                val geoiqTrack = GeoIQTrack.fromLiveKitTrack(event.track)
                GeoIQRoomEvent.TrackUnsubscribed(geoiqTrack, GeoIQParticipant(event.participant))
            }
            // Ignore other events that we don't want to expose
            else -> null
        }
    }
}
