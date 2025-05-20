package com.geoiq.lk.test

import com.geoiq.lk.events.GeoIQRoomEvent
import com.geoiq.lk.room.participant.GeoIQParticipant
import com.geoiq.lk.track.GeoIQAudioTrack
import com.geoiq.lk.track.GeoIQTrack
import com.geoiq.lk.track.GeoIQVideoTrack
import com.geoiq.lk.types.GeoIQTrackSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Unit tests for the GeoIQ-LK SDK event and track classes.
 */
class GeoIQLKEventTrackTest {

    @Test
    fun testRoomEvents() {
        // Test Connected event
        val connectedEvent = GeoIQRoomEvent.Connected
        assertTrue(connectedEvent is GeoIQRoomEvent.Connected)
        
        // Test Disconnected event
        val disconnectedEvent = GeoIQRoomEvent.Disconnected
        assertTrue(disconnectedEvent is GeoIQRoomEvent.Disconnected)
        
        // Test ParticipantConnected event
        val mockParticipant = mock(GeoIQParticipant::class.java)
        val participantConnectedEvent = GeoIQRoomEvent.ParticipantConnected(mockParticipant)
        assertTrue(participantConnectedEvent is GeoIQRoomEvent.ParticipantConnected)
        assertEquals(mockParticipant, participantConnectedEvent.participant)
        
        // Test TrackSubscribed event
        val mockTrack = mock(GeoIQTrack::class.java)
        val trackSubscribedEvent = GeoIQRoomEvent.TrackSubscribed(mockTrack, mockParticipant)
        assertTrue(trackSubscribedEvent is GeoIQRoomEvent.TrackSubscribed)
        assertEquals(mockTrack, trackSubscribedEvent.track)
        assertEquals(mockParticipant, trackSubscribedEvent.participant)
    }
    
    @Test
    fun testTrackTypes() {
        // Test that track types are correctly defined
        val mockAudioTrack = mock(GeoIQAudioTrack::class.java)
        val mockVideoTrack = mock(GeoIQVideoTrack::class.java)
        
        assertTrue(mockAudioTrack is GeoIQTrack)
        assertTrue(mockVideoTrack is GeoIQTrack)
    }
}
