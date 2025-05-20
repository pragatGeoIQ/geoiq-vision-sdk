package com.geoiq.lk.test

import android.content.Context
import com.geoiq.lk.GeoIQLK
import com.geoiq.lk.GeoIQRoom
import com.geoiq.lk.audio.GeoIQAudioMode
import com.geoiq.lk.audio.GeoIQAudioOptions
import com.geoiq.lk.events.GeoIQRoomEvent
import com.geoiq.lk.renderer.GeoIQVideoView
import com.geoiq.lk.room.GeoIQRoomOptions
import com.geoiq.lk.track.GeoIQVideoTrack
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Test class to validate the GeoIQ-LK SDK functionality.
 * This is a simple example showing how to use the SDK.
 */
class GeoIQLKTest(private val context: Context) {

    private lateinit var room: GeoIQRoom
    private lateinit var videoView: GeoIQVideoView
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    /**
     * Initialize the SDK and connect to a room.
     */
    fun testSDKFunctionality() {
        // Create video view
        videoView = GeoIQVideoView(context)
        videoView.initialize()
        
        // Create room with custom audio options
        val audioOptions = GeoIQAudioOptions(
            audioMode = GeoIQAudioMode.COMMUNICATION
        )
        room = GeoIQLK.create(context, audioOptions)
        
        // Initialize video renderer
        room.initVideoRenderer(videoView)
        
        // Connect to room with options
        val roomOptions = GeoIQRoomOptions(
            autoSubscribe = true,
            adaptiveStream = true,
            dynacast = true
        )
        
        coroutineScope.launch {
            // Set up event handling
            launch {
                room.events.collect { event ->
                    when (event) {
                        is GeoIQRoomEvent.Connected -> {
                            println("Connected to room")
                        }
                        is GeoIQRoomEvent.ParticipantConnected -> {
                            println("Participant connected: ${event.participant.identity}")
                        }
                        is GeoIQRoomEvent.TrackSubscribed -> {
                            handleTrackSubscribed(event)
                        }
                        else -> {
                            // Handle other events
                        }
                    }
                }
            }
            
            // Connect to the room
            try {
                room.connect(
                    url = "wss://example.livekit.io",
                    token = "your-token",
                    options = roomOptions
                )
                
                // Enable camera and microphone
                room.localParticipant.setCameraEnabled(true)
                room.localParticipant.setMicrophoneEnabled(true)
            } catch (e: Exception) {
                println("Error connecting to room: ${e.message}")
            }
        }
    }
    
    private fun handleTrackSubscribed(event: GeoIQRoomEvent.TrackSubscribed) {
        val track = event.track
        if (track is GeoIQVideoTrack) {
            // Add renderer to video track
            track.addRenderer(videoView)
        }
    }
    
    /**
     * Disconnect from the room and release resources.
     */
    fun cleanup() {
        room.disconnect()
        videoView.release()
    }
}
