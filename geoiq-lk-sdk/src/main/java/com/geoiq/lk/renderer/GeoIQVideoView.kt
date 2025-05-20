package com.geoiq.lk.renderer

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

/**
 * A video renderer view for GeoIQ-LK.
 * This class wraps the WebRTC SurfaceViewRenderer and provides a simplified API.
 */
class GeoIQVideoView(context: Context) : FrameLayout(context) {
    
    private val surfaceViewRenderer = SurfaceViewRenderer(context)
    
    init {
        // Add the SurfaceViewRenderer to this view
        addView(surfaceViewRenderer, LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ))
    }
    
    /**
     * Initialize the video view.
     */
    fun initialize() {
        surfaceViewRenderer.init(null, RendererCommon.RendererEvents {
            // Handle renderer events if needed
        })
    }
    
    /**
     * Release resources used by the video view.
     */
    fun release() {
        surfaceViewRenderer.release()
    }
    
    /**
     * Set the scaling type for the video.
     *
     * @param scalingType The scaling type to use
     */
    fun setScalingType(scalingType: GeoIQScalingType) {
        val webrtcScalingType = when (scalingType) {
            GeoIQScalingType.SCALE_ASPECT_FIT -> RendererCommon.ScalingType.SCALE_ASPECT_FIT
            GeoIQScalingType.SCALE_ASPECT_FILL -> RendererCommon.ScalingType.SCALE_ASPECT_FILL
            GeoIQScalingType.SCALE_ASPECT_BALANCED -> RendererCommon.ScalingType.SCALE_ASPECT_BALANCED
        }
        surfaceViewRenderer.setScalingType(webrtcScalingType)
    }
    
    /**
     * Get the internal SurfaceViewRenderer.
     * This method is internal to the SDK and not exposed in public documentation.
     */
    internal fun getInternalRenderer(): SurfaceViewRenderer {
        return surfaceViewRenderer
    }
}

/**
 * Scaling types for video rendering.
 */
enum class GeoIQScalingType {
    /**
     * Scale the video uniformly to fit the view, potentially leaving black bars.
     */
    SCALE_ASPECT_FIT,
    
    /**
     * Scale the video uniformly to fill the view, potentially cropping the video.
     */
    SCALE_ASPECT_FILL,
    
    /**
     * Balance between fitting and filling the view.
     */
    SCALE_ASPECT_BALANCED
}
