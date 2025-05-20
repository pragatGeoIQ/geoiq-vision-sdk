package com.geoiq.lk.test

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.geoiq.lk.GeoIQLK
import com.geoiq.lk.GeoIQRoom
import com.geoiq.lk.audio.GeoIQAudioMode
import com.geoiq.lk.audio.GeoIQAudioOptions
import com.geoiq.lk.room.GeoIQRoomOptions
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for the GeoIQ-LK SDK.
 */
@RunWith(AndroidJUnit4::class)
class GeoIQLKInstrumentedTest {

    private lateinit var appContext: Context

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testCreateRoom() {
        // Test basic room creation
        val room = GeoIQLK.create(appContext)
        assertNotNull("Room should not be null", room)
    }

    @Test
    fun testCreateRoomWithAudioOptions() {
        // Test room creation with audio options
        val audioOptions = GeoIQAudioOptions(
            audioMode = GeoIQAudioMode.COMMUNICATION
        )
        val room = GeoIQLK.create(appContext, audioOptions)
        assertNotNull("Room with audio options should not be null", room)
    }

    @Test
    fun testRoomOptions() {
        // Test room options creation
        val options = GeoIQRoomOptions(
            autoSubscribe = false,
            adaptiveStream = true,
            dynacast = false
        )
        
        // Verify options are set correctly
        assert(!options.autoSubscribe)
        assert(options.adaptiveStream)
        assert(!options.dynacast)
    }
}
