package com.geoiq.lk.test

import com.geoiq.lk.audio.GeoIQAudioMode
import com.geoiq.lk.audio.GeoIQAudioOptions
import com.geoiq.lk.renderer.GeoIQScalingType
import com.geoiq.lk.room.GeoIQRoomOptions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the GeoIQ-LK SDK configuration classes.
 */
class GeoIQLKConfigTest {

    @Test
    fun testAudioOptions() {
        // Test default audio options
        val defaultOptions = GeoIQAudioOptions()
        assertEquals(GeoIQAudioMode.COMMUNICATION, defaultOptions.audioMode)
        
        // Test custom audio options
        val mediaOptions = GeoIQAudioOptions(GeoIQAudioMode.MEDIA)
        assertEquals(GeoIQAudioMode.MEDIA, mediaOptions.audioMode)
        
        // Test conversion to LiveKit options (internal method)
        val livekitOptions = mediaOptions.toLiveKitAudioOptions()
        assertTrue(livekitOptions.audioOutputType is io.livekit.android.audio.AudioType.MediaAudioType)
    }

    @Test
    fun testRoomOptions() {
        // Test default room options
        val defaultOptions = GeoIQRoomOptions()
        assertTrue(defaultOptions.autoSubscribe)
        assertTrue(defaultOptions.adaptiveStream)
        assertTrue(defaultOptions.dynacast)
        
        // Test custom room options
        val customOptions = GeoIQRoomOptions(
            autoSubscribe = false,
            adaptiveStream = false,
            dynacast = false
        )
        assertTrue(!customOptions.autoSubscribe)
        assertTrue(!customOptions.adaptiveStream)
        assertTrue(!customOptions.dynacast)
        
        // Test conversion to LiveKit options (internal method)
        val livekitOptions = customOptions.toLiveKitRoomOptions()
        assertTrue(!livekitOptions.autoSubscribe)
        assertTrue(!livekitOptions.adaptiveStream)
        assertTrue(!livekitOptions.dynacast)
    }
    
    @Test
    fun testScalingTypes() {
        // Test scaling types
        val scalingTypes = GeoIQScalingType.values()
        assertEquals(3, scalingTypes.size)
        assertEquals(GeoIQScalingType.SCALE_ASPECT_FIT, scalingTypes[0])
        assertEquals(GeoIQScalingType.SCALE_ASPECT_FILL, scalingTypes[1])
        assertEquals(GeoIQScalingType.SCALE_ASPECT_BALANCED, scalingTypes[2])
    }
}
