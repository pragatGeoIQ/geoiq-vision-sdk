package com.geoiq.lk

import io.livekit.android.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import com.geoiq.lk.room.participant.GeoIQLocalParticipant
import com.geoiq.lk.room.participant.GeoIQParticipant
import com.geoiq.lk.events.GeoIQRoomEvent
import com.geoiq.lk.events.internal.EventConverter
import com.geoiq.lk.annotations.FlowProperty
import com.geoiq.lk.renderer.GeoIQVideoView
import com.geoiq.lk.room.GeoIQRoomOptions
import com.geoiq.lk.types.GeoIQConnectionState

/**
 * Represents a connection to a GeoIQ room.
 */
class GeoIQRoom internal constructor(private val livekitRoom: Room) {
    
    /**
     * The local participant in the room.
     */
    val localParticipant: GeoIQLocalParticipant
        get() = GeoIQLocalParticipant(livekitRoom.localParticipant)
    
    /**
     * The current connection state of the room.
     */
    val connectionState: FlowProperty<GeoIQConnectionState> = FlowProperty(
        GeoIQConnectionState.DISCONNECTED
    )
    
    /**
     * Flow of room events.
     */
    val events: Flow<GeoIQRoomEvent> = livekitRoom.events
        .mapNotNull { EventConverter.convertEvent(it) }
        .flowOn(Dispatchers.Default)
    
    init {
        // Initialize the connection state flow
        livekitRoom.connectionState.observe { state ->
            connectionState.value = GeoIQConnectionState.fromLiveKitState(state)
        }
    }
    
    /**
     * Connect to a room.
     *
     * @param url The URL of the LiveKit server
     * @param token The access token for the room
     */
    suspend fun connect(url: String, token: String) {
        livekitRoom.connect(url, token)
    }
    
    /**
     * Connect to a room with options.
     *
     * @param url The URL of the LiveKit server
     * @param token The access token for the room
     * @param options Options for the room connection
     */
    suspend fun connect(
        url: String, 
        token: String,
        options: GeoIQRoomOptions = GeoIQRoomOptions()
    ) {
        livekitRoom.connect(
            url = url,
            token = token,
            roomOptions = options.toLiveKitRoomOptions()
        )
    }
    
    /**
     * Disconnect from the room.
     */
    fun disconnect() {
        livekitRoom.disconnect()
    }
    
    /**
     * Initialize a video renderer for use with this room.
     *
     * @param videoView The GeoIQVideoView to initialize
     */
    fun initVideoRenderer(videoView: GeoIQVideoView) {
        livekitRoom.initVideoRenderer(videoView.getInternalRenderer())
    }
}
