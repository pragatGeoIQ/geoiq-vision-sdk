package com.geoiq.lk.track

import io.livekit.android.room.track.Track
import io.livekit.android.room.track.AudioTrack
import io.livekit.android.room.track.VideoTrack
import com.geoiq.lk.renderer.GeoIQVideoView
import com.geoiq.lk.types.GeoIQTrackSource

/**
 * Represents a media track in a GeoIQ room.
 */
sealed class GeoIQTrack {
    
    /**
     * The track's name.
     */
    abstract val name: String
    
    /**
     * The track's kind (audio or video).
     */
    abstract val kind: String
    
    /**
     * The track's source.
     */
    abstract val source: GeoIQTrackSource
    
    /**
     * Companion object for creating GeoIQTrack instances from LiveKit tracks.
     */
    companion object {
        /**
         * Create a GeoIQTrack from a LiveKit Track.
         */
        internal fun fromLiveKitTrack(track: Track): GeoIQTrack {
            return when (track) {
                is AudioTrack -> GeoIQAudioTrack(track)
                is VideoTrack -> GeoIQVideoTrack(track)
                else -> throw IllegalArgumentException("Unknown track type")
            }
        }
    }
}

/**
 * Represents an audio track in a GeoIQ room.
 */
class GeoIQAudioTrack internal constructor(
    private val audioTrack: AudioTrack
) : GeoIQTrack() {
    
    override val name: String
        get() = audioTrack.name
    
    override val kind: String
        get() = "audio"
        
    override val source: GeoIQTrackSource
        get() = GeoIQTrackSource.fromLiveKitSource(audioTrack.source)
}

/**
 * Represents a video track in a GeoIQ room.
 */
class GeoIQVideoTrack internal constructor(
    private val videoTrack: VideoTrack
) : GeoIQTrack() {
    
    override val name: String
        get() = videoTrack.name
    
    override val kind: String
        get() = "video"
        
    override val source: GeoIQTrackSource
        get() = GeoIQTrackSource.fromLiveKitSource(videoTrack.source)
    
    /**
     * Add a renderer to this video track.
     *
     * @param renderer The GeoIQVideoView to add
     */
    fun addRenderer(renderer: GeoIQVideoView) {
        videoTrack.addRenderer(renderer.getInternalRenderer())
    }
    
    /**
     * Remove a renderer from this video track.
     *
     * @param renderer The GeoIQVideoView to remove
     */
    fun removeRenderer(renderer: GeoIQVideoView) {
        videoTrack.removeRenderer(renderer.getInternalRenderer())
    }
}
