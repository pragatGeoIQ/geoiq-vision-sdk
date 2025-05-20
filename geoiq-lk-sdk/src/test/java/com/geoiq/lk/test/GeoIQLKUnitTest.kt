package com.geoiq.lk.test

import com.geoiq.lk.types.GeoIQConnectionState
import com.geoiq.lk.types.GeoIQTrackSource
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for the GeoIQ-LK SDK.
 */
class GeoIQLKUnitTest {

    @Test
    fun testConnectionStateValues() {
        // Test that all connection states are properly defined
        val states = GeoIQConnectionState.values()
        assertEquals(4, states.size)
        assertEquals(GeoIQConnectionState.DISCONNECTED, states[0])
        assertEquals(GeoIQConnectionState.CONNECTING, states[1])
        assertEquals(GeoIQConnectionState.CONNECTED, states[2])
        assertEquals(GeoIQConnectionState.RECONNECTING, states[3])
    }

    @Test
    fun testTrackSourceValues() {
        // Test that all track sources are properly defined
        val sources = GeoIQTrackSource.values()
        assertEquals(4, sources.size)
        assertEquals(GeoIQTrackSource.UNKNOWN, sources[0])
        assertEquals(GeoIQTrackSource.CAMERA, sources[1])
        assertEquals(GeoIQTrackSource.MICROPHONE, sources[2])
        assertEquals(GeoIQTrackSource.SCREEN_SHARE, sources[3])
    }
}
