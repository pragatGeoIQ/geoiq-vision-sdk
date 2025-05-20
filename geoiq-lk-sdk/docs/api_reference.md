# API Reference for GeoIQ-LK SDK

This document provides detailed API reference for all public classes and methods in the GeoIQ-LK SDK.

## Table of Contents

- [GeoIQLK](#geoiqlk)
- [GeoIQRoom](#geoiqroom)
- [GeoIQParticipant](#geoiqparticipant)
- [GeoIQLocalParticipant](#geoiqlocalparticipant)
- [GeoIQTrack](#geoiqtrack)
- [GeoIQVideoTrack](#geoiqvideotrack)
- [GeoIQAudioTrack](#geoiqaudiotrack)
- [GeoIQVideoView](#geoiqvideoview)
- [GeoIQRoomEvent](#geoiqroomevent)
- [GeoIQRoomOptions](#geoiqroomoptions)
- [GeoIQAudioOptions](#geoiqaudiooptions)
- [GeoIQScalingType](#geoiqscalingtype)
- [GeoIQTrackSource](#geoiqtracksource)
- [GeoIQConnectionState](#geoiqconnectionstate)

## GeoIQLK

Main entry point for the GeoIQ-LK SDK.

### Methods

#### `create(context: Context): GeoIQRoom`

Creates a new GeoIQ Room instance.

**Parameters:**
- `context`: Android application context

**Returns:**
- A new `GeoIQRoom` instance

#### `create(context: Context, audioOptions: GeoIQAudioOptions): GeoIQRoom`

Creates a new GeoIQ Room instance with custom audio options.

**Parameters:**
- `context`: Android application context
- `audioOptions`: Custom audio configuration options

**Returns:**
- A new `GeoIQRoom` instance

## GeoIQRoom

Represents a connection to a GeoIQ room.

### Properties

#### `localParticipant: GeoIQLocalParticipant`

The local participant in the room.

#### `connectionState: FlowProperty<GeoIQConnectionState>`

The current connection state of the room.

#### `events: Flow<GeoIQRoomEvent>`

Flow of room events.

### Methods

#### `connect(url: String, token: String)`

Connect to a room.

**Parameters:**
- `url`: The URL of the server
- `token`: The access token for the room

#### `connect(url: String, token: String, options: GeoIQRoomOptions)`

Connect to a room with custom options.

**Parameters:**
- `url`: The URL of the server
- `token`: The access token for the room
- `options`: Custom room connection options

#### `disconnect()`

Disconnect from the room.

#### `initVideoRenderer(videoView: GeoIQVideoView)`

Initialize a video renderer for use with this room.

**Parameters:**
- `videoView`: The GeoIQVideoView to initialize

## GeoIQParticipant

Represents a participant in a GeoIQ room.

### Properties

#### `identity: String`

The participant's identity.

#### `name: String?`

The participant's name.

#### `isSpeaking: Boolean`

Whether the participant is speaking.

#### `tracks: Flow<List<GeoIQTrack>>`

Flow of the participant's tracks.

## GeoIQLocalParticipant

Represents the local participant in a GeoIQ room. Extends `GeoIQParticipant`.

### Methods

#### `setCameraEnabled(enabled: Boolean)`

Enable or disable the camera.

**Parameters:**
- `enabled`: Whether the camera should be enabled

#### `setMicrophoneEnabled(enabled: Boolean)`

Enable or disable the microphone.

**Parameters:**
- `enabled`: Whether the microphone should be enabled

#### `setScreenShareEnabled(enabled: Boolean, mediaProjectionPermissionResultData: Intent? = null)`

Enable or disable screen sharing.

**Parameters:**
- `enabled`: Whether screen sharing should be enabled
- `mediaProjectionPermissionResultData`: The result data from the screen capture intent

## GeoIQTrack

Base class for media tracks.

### Properties

#### `name: String`

The track's name.

#### `kind: String`

The track's kind (audio or video).

#### `source: GeoIQTrackSource`

The track's source.

## GeoIQVideoTrack

Represents a video track. Extends `GeoIQTrack`.

### Methods

#### `addRenderer(renderer: GeoIQVideoView)`

Add a renderer to this video track.

**Parameters:**
- `renderer`: The GeoIQVideoView to add

#### `removeRenderer(renderer: GeoIQVideoView)`

Remove a renderer from this video track.

**Parameters:**
- `renderer`: The GeoIQVideoView to remove

## GeoIQAudioTrack

Represents an audio track. Extends `GeoIQTrack`.

## GeoIQVideoView

A view for rendering video tracks.

### Methods

#### `initialize()`

Initialize the video view.

#### `release()`

Release resources used by the video view.

#### `setScalingType(scalingType: GeoIQScalingType)`

Set the scaling type for the video.

**Parameters:**
- `scalingType`: The scaling type to use

## GeoIQRoomEvent

Base class for room events.

### Subclasses

#### `GeoIQRoomEvent.Connected`

The room connection has been established.

#### `GeoIQRoomEvent.Disconnected`

The room connection has been closed.

#### `GeoIQRoomEvent.ParticipantConnected`

A participant has connected to the room.

**Properties:**
- `participant: GeoIQParticipant`: The participant that connected

#### `GeoIQRoomEvent.ParticipantDisconnected`

A participant has disconnected from the room.

**Properties:**
- `participant: GeoIQParticipant`: The participant that disconnected

#### `GeoIQRoomEvent.TrackSubscribed`

A track has been subscribed to.

**Properties:**
- `track: GeoIQTrack`: The track that was subscribed to
- `participant: GeoIQParticipant`: The participant that owns the track

#### `GeoIQRoomEvent.TrackUnsubscribed`

A track has been unsubscribed from.

**Properties:**
- `track: GeoIQTrack`: The track that was unsubscribed from
- `participant: GeoIQParticipant`: The participant that owns the track

## GeoIQRoomOptions

Options for connecting to a GeoIQ room.

### Properties

#### `autoSubscribe: Boolean`

Whether to automatically subscribe to tracks when they are published. Default: `true`

#### `adaptiveStream: Boolean`

Whether to enable adaptive stream quality. Default: `true`

#### `dynacast: Boolean`

Whether to enable dynacast (dynamic video quality adjustment). Default: `true`

## GeoIQAudioOptions

Audio options for the GeoIQ room.

### Properties

#### `audioMode: GeoIQAudioMode`

The audio mode to use. Default: `GeoIQAudioMode.COMMUNICATION`

## GeoIQScalingType

Scaling types for video rendering.

### Values

#### `SCALE_ASPECT_FIT`

Scale the video uniformly to fit the view, potentially leaving black bars.

#### `SCALE_ASPECT_FILL`

Scale the video uniformly to fill the view, potentially cropping the video.

#### `SCALE_ASPECT_BALANCED`

Balance between fitting and filling the view.

## GeoIQTrackSource

Source of a track.

### Values

#### `UNKNOWN`

Unknown source.

#### `CAMERA`

Camera source.

#### `MICROPHONE`

Microphone source.

#### `SCREEN_SHARE`

Screen share source.

## GeoIQConnectionState

Connection state of a room.

### Values

#### `DISCONNECTED`

The room is disconnected.

#### `CONNECTING`

The room is connecting.

#### `CONNECTED`

The room is connected.

#### `RECONNECTING`

The room is reconnecting after a connection loss.

## GeoIQAudioMode

Audio mode for the GeoIQ room.

### Values

#### `COMMUNICATION`

Optimized for voice communication.

#### `MEDIA`

Optimized for media playback.
