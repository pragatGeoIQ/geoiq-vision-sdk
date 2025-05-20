package com.geoiq.lk.room

/**
 * Options for connecting to a GeoIQ room.
 */
data class GeoIQRoomOptions(
    /**
     * Whether to automatically subscribe to tracks when they are published.
     */
    val autoSubscribe: Boolean = true,
    
    /**
     * Whether to enable adaptive stream quality.
     */
    val adaptiveStream: Boolean = true,
    
    /**
     * Whether to enable dynacast (dynamic video quality adjustment).
     */
    val dynacast: Boolean = true
) {
    /**
     * Convert to LiveKit room options.
     * This method is internal to the SDK and not exposed in public documentation.
     */
    internal fun toLiveKitRoomOptions(): io.livekit.android.room.RoomOptions {
        return io.livekit.android.room.RoomOptions(
            autoSubscribe = this.autoSubscribe,
            adaptiveStream = this.adaptiveStream,
            dynacast = this.dynacast
        )
    }
}
