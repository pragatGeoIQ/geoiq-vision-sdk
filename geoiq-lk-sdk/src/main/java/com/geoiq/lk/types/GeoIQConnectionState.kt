package com.geoiq.lk.types

import io.livekit.android.room.ConnectionState as LiveKitConnectionState

/**
 * Connection state of a room.
 */
enum class GeoIQConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING;
    
    internal companion object {
        fun fromLiveKitState(state: LiveKitConnectionState): GeoIQConnectionState {
            return when (state) {
                LiveKitConnectionState.DISCONNECTED -> DISCONNECTED
                LiveKitConnectionState.CONNECTING -> CONNECTING
                LiveKitConnectionState.CONNECTED -> CONNECTED
                LiveKitConnectionState.RECONNECTING -> RECONNECTING
            }
        }
    }
}
