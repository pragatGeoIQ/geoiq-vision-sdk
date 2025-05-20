# GeoIQ-LK SDK Documentation

Welcome to the GeoIQ-LK SDK documentation. This SDK provides a streamlined interface for adding real-time audio and video capabilities to your Android applications.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Installation](#installation)
3. [Basic Usage](#basic-usage)
4. [Core Components](#core-components)
5. [Advanced Usage](#advanced-usage)
6. [API Reference](#api-reference)
7. [Troubleshooting](#troubleshooting)

## Getting Started

GeoIQ-LK is an Android SDK that enables real-time audio and video communication in your applications. With a simple API, you can quickly implement features like video calls, audio streaming, and screen sharing.

### Requirements

- Android API level 21 (Android 5.0) or higher
- Java 8 or higher
- Kotlin 1.5 or higher

## Installation

Add the GeoIQ-LK SDK to your project by including it in your app's `build.gradle` file:

```gradle
dependencies {
    implementation 'com.geoiq:geoiq-lk:1.0.0'
}
```

Make sure you have the following repositories in your project's `settings.gradle` file:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

## Basic Usage

### Permissions

GeoIQ-LK requires camera and microphone permissions. Add these to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
```

You'll also need to request these permissions at runtime:

```kotlin
// Request permissions
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
        MY_PERMISSIONS_REQUEST
    )
}
```

### Connecting to a Room

```kotlin
import com.geoiq.lk.GeoIQLK
import com.geoiq.lk.GeoIQRoom
import com.geoiq.lk.events.GeoIQRoomEvent
import com.geoiq.lk.renderer.GeoIQVideoView

class MainActivity : AppCompatActivity() {

    private lateinit var room: GeoIQRoom
    private lateinit var videoView: GeoIQVideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize video view
        videoView = findViewById(R.id.video_view)
        videoView.initialize()
        
        // Create room
        room = GeoIQLK.create(applicationContext)
        room.initVideoRenderer(videoView)
        
        connectToRoom()
    }
    
    private fun connectToRoom() {
        val url = "wss://your-server-url.com"
        val token = "your-access-token"
        
        lifecycleScope.launch {
            // Set up event handling
            launch {
                room.events.collect { event ->
                    when (event) {
                        is GeoIQRoomEvent.Connected -> {
                            Log.d("GeoIQ", "Connected to room")
                        }
                        is GeoIQRoomEvent.TrackSubscribed -> {
                            handleTrackSubscribed(event)
                        }
                        // Handle other events
                    }
                }
            }
            
            // Connect to the room
            room.connect(url, token)
            
            // Enable camera and microphone
            room.localParticipant.setCameraEnabled(true)
            room.localParticipant.setMicrophoneEnabled(true)
        }
    }
    
    private fun handleTrackSubscribed(event: GeoIQRoomEvent.TrackSubscribed) {
        val track = event.track
        if (track is GeoIQVideoTrack) {
            track.addRenderer(videoView)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        room.disconnect()
        videoView.release()
    }
}
```

## Core Components

### GeoIQLK

The main entry point for the SDK. Use it to create a `GeoIQRoom` instance.

```kotlin
val room = GeoIQLK.create(context)
```

### GeoIQRoom

Represents a connection to a room. Manages the connection lifecycle and provides access to participants and events.

```kotlin
// Connect to a room
room.connect(url, token)

// Disconnect from a room
room.disconnect()

// Access the local participant
val localParticipant = room.localParticipant

// Listen for room events
lifecycleScope.launch {
    room.events.collect { event ->
        // Handle events
    }
}
```

### GeoIQParticipant

Represents a participant in a room. Can be either a local participant or a remote participant.

```kotlin
// Get participant identity
val identity = participant.identity

// Get participant name
val name = participant.name

// Check if participant is speaking
val isSpeaking = participant.isSpeaking

// Get participant tracks
lifecycleScope.launch {
    participant.tracks.collect { tracks ->
        // Handle tracks
    }
}
```

### GeoIQLocalParticipant

Represents the local participant in a room. Extends `GeoIQParticipant` with methods for publishing local media.

```kotlin
// Enable/disable camera
localParticipant.setCameraEnabled(true)

// Enable/disable microphone
localParticipant.setMicrophoneEnabled(true)

// Enable/disable screen sharing
val screenCaptureIntentLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == Activity.RESULT_OK && result.data != null) {
        lifecycleScope.launch {
            localParticipant.setScreenShareEnabled(true, result.data)
        }
    }
}

// Launch screen capture intent
val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
screenCaptureIntentLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
```

### GeoIQTrack

Base class for media tracks. Can be either an audio track or a video track.

```kotlin
// Get track name
val name = track.name

// Get track kind (audio or video)
val kind = track.kind

// Get track source
val source = track.source
```

### GeoIQVideoTrack

Represents a video track. Extends `GeoIQTrack` with methods for rendering video.

```kotlin
// Add a renderer to the video track
videoTrack.addRenderer(videoView)

// Remove a renderer from the video track
videoTrack.removeRenderer(videoView)
```

### GeoIQAudioTrack

Represents an audio track. Extends `GeoIQTrack`.

### GeoIQVideoView

A view for rendering video tracks.

```kotlin
// Initialize the video view
videoView.initialize()

// Release resources
videoView.release()

// Set scaling type
videoView.setScalingType(GeoIQScalingType.SCALE_ASPECT_FIT)
```

### GeoIQRoomEvent

Base class for room events. Subclasses include:

- `GeoIQRoomEvent.Connected`: The room connection has been established
- `GeoIQRoomEvent.Disconnected`: The room connection has been closed
- `GeoIQRoomEvent.ParticipantConnected`: A participant has connected to the room
- `GeoIQRoomEvent.ParticipantDisconnected`: A participant has disconnected from the room
- `GeoIQRoomEvent.TrackSubscribed`: A track has been subscribed to
- `GeoIQRoomEvent.TrackUnsubscribed`: A track has been unsubscribed from

## Advanced Usage

### Room Options

You can customize the room connection with options:

```kotlin
val options = GeoIQRoomOptions(
    autoSubscribe = true,
    adaptiveStream = true,
    dynacast = true
)

room.connect(url, token, options)
```

### Audio Configuration

Configure audio behavior:

```kotlin
val audioOptions = GeoIQAudioOptions(
    audioMode = GeoIQAudioMode.COMMUNICATION
)

val room = GeoIQLK.create(
    context = applicationContext,
    audioOptions = audioOptions
)
```

### Video Scaling

Control how video is scaled in the renderer:

```kotlin
videoView.setScalingType(GeoIQScalingType.SCALE_ASPECT_FIT) // Fit within view
videoView.setScalingType(GeoIQScalingType.SCALE_ASPECT_FILL) // Fill view (may crop)
videoView.setScalingType(GeoIQScalingType.SCALE_ASPECT_BALANCED) // Balance between fit and fill
```

## API Reference

For detailed API documentation, please refer to the [API Reference](api_reference.md).

## Troubleshooting

### Common Issues

#### Camera or Microphone Not Working

- Ensure you've requested and been granted the necessary permissions
- Check that no other app is using the camera or microphone
- Verify that the device has a camera and microphone

#### Connection Issues

- Check your network connection
- Verify that the server URL and token are correct
- Ensure the token has not expired

#### High CPU or Memory Usage

- Release resources when they're no longer needed
- Disconnect from rooms when leaving the app
- Release video renderers in onDestroy()

### Getting Help

If you encounter issues not covered here, please contact GeoIQ support at support@geoiq.com.
