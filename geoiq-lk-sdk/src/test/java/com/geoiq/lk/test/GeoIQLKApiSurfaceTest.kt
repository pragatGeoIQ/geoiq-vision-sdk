package com.geoiq.lk.test

import com.geoiq.lk.GeoIQLK
import com.geoiq.lk.GeoIQRoom
import com.geoiq.lk.annotations.FlowProperty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Unit tests for the GeoIQ-LK SDK API surface validation.
 */
class GeoIQLKApiSurfaceTest {

    @Test
    fun testApiSurfaceExposure() {
        // Test that the main entry point class is correctly defined
        val geoiqLkClass = GeoIQLK::class.java
        assertNotNull(geoiqLkClass)
        
        // Test that the Room class is correctly defined
        val roomClass = GeoIQRoom::class.java
        assertNotNull(roomClass)
        
        // Test that the FlowProperty class is correctly defined
        val flowPropertyClass = FlowProperty::class.java
        assertNotNull(flowPropertyClass)
    }
    
    @Test
    fun testNoUpstreamTypesExposed() {
        // This test verifies that no LiveKit types are exposed in the public API
        
        // Get all public methods from GeoIQRoom
        val roomMethods = GeoIQRoom::class.java.methods
        
        // Check that none of the method return types or parameter types contain "livekit" in their package name
        for (method in roomMethods) {
            val returnType = method.returnType
            assert(!returnType.name.contains("livekit")) { 
                "Method ${method.name} returns LiveKit type: ${returnType.name}" 
            }
            
            val parameterTypes = method.parameterTypes
            for (paramType in parameterTypes) {
                assert(!paramType.name.contains("livekit")) { 
                    "Method ${method.name} has LiveKit parameter type: ${paramType.name}" 
                }
            }
        }
    }
}
