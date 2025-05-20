package com.geoiq.geoiq_android_lk_vision_bot_sdk // Your app's package

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.geoiq.geoiq_android_lk_vision_bot_sdk.ui.theme.GEOIQANDROIDLKVISIONBOTSDKTheme
// Ensure VisionBotSDKManager and GeoVisionEvent are correctly imported from your SDK module
// If VisionBotSDKManager is in the same module and package, this import might not be strictly needed,
// but it's good practice for clarity if it were in a separate library module.
// import com.geoiq.geoiq_android_lk_vision_bot_sdk.GeoVisionEvent
// import com.geoiq.geoiq_android_lk_vision_bot_sdk.VisionBotSDKManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("MainActivityPermissions", "${it.key} = ${it.value}")
            }
            // Handle permission results if needed, e.g., show a message if not granted
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permissions
        val permissionsToRequest = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET
        )
        val permissionsNotGranted = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsNotGranted.isNotEmpty()) {
            requestMultiplePermissionsLauncher.launch(permissionsNotGranted.toTypedArray())
        }

        setContent {
            GEOIQANDROIDLKVISIONBOTSDKTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SDKInteractionScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Important: Shutdown the SDK when the activity is destroyed
        // to release resources and cancel coroutines.
        VisionBotSDKManager.shutdown()
        Log.d("MainActivity", "VisionBotSDKManager shutdown called.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SDKInteractionScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var livekitUrl by remember { mutableStateOf("") } // TODO: Replace with your URL
    var accessToken by remember { mutableStateOf("") } // TODO: Replace with your token

    var eventLog by remember { mutableStateOf(listOf<String>()) }
    var connectionStatus by remember { mutableStateOf("Disconnected") }
    var isConnecting by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }

    var isCameraEnabledUi by remember { mutableStateOf(false) }
    var isMicrophoneEnabledUi by remember { mutableStateOf(false) }

    fun addLog(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        eventLog = eventLog + "[$timestamp] $message"
        Log.d("SDKInteractionScreen", message)
    }

    // Collect events from the SDK
    LaunchedEffect(Unit) {



        VisionBotSDKManager.events.collect { event ->
            addLog("Vinay Event: $event")
            when (event) {

                is GeoVisionEvent.Connecting -> {
                    addLog("Vinay Connecting to ${event.url} with token ending in ...${event.tokenSnippet}")
                    connectionStatus = "Connecting..."
                    isConnecting = true
                    isConnected = false
                }
                is GeoVisionEvent.Connected -> {
                    addLog("Vinay Connected to room: ${event.roomName}. Local User: ${event.localParticipant.identity}")
                    connectionStatus = "Connected: ${event.roomName}"
                    isConnecting = false
                    isConnected = true
                    // Update UI based on initial SDK state after connection
                    isCameraEnabledUi = VisionBotSDKManager.isCameraEnabled()
                    isMicrophoneEnabledUi = VisionBotSDKManager.isMicrophoneEnabled()
                }
                is GeoVisionEvent.Disconnected -> {
                    addLog("Vinay Disconnected. Reason: ${event.reason ?: "Client initiated"}")
                    connectionStatus = "Disconnected"
                    isConnecting = false
                    isConnected = false
                }
                is GeoVisionEvent.ParticipantJoined -> {
                    addLog("Vinay Participant joined: ${event.participant.identity}")
                }
                is GeoVisionEvent.ParticipantLeft -> {
                    addLog("Vinay Participant left: ${event.participant.identity}")
                }
                is GeoVisionEvent.TrackPublished -> {
                    addLog("Vinay Local track published: ${event.publication.source} by ${event.participant.identity}")
                    if (event.participant.isSpeaking) {//avinash


                        when (event.publication.source) {
                            io.livekit.android.room.track.Track.Source.CAMERA -> isCameraEnabledUi = true
                            io.livekit.android.room.track.Track.Source.MICROPHONE -> isMicrophoneEnabledUi = true
                            else -> {}
                        }
                    }
                }
                // Note: You might also want to handle TrackUnpublished for local tracks
                // to update isCameraEnabledUi and isMicrophoneEnabledUi to false.
                // For simplicity, this example relies on the setCameraEnabled/setMicrophoneEnabled
                // calls and initial Connected state. A more robust solution would listen
                // to local unpublish events as well. Consider this:
                // is GeoVisionEvent.TrackUnpublished -> {
                //    addLog("Local track unpublished: ${event.publication.source} by ${event.participant.identity}")
                //    if (event.participant.isLocal) {
                //        when (event.publication.source) {
                //            io.livekit.android.room.track.Track.Source.CAMERA -> isCameraEnabledUi = false
                //            io.livekit.android.room.track.Track.Source.MICROPHONE -> isMicrophoneEnabledUi = false
                //            else -> {}
                //        }
                //    }
                // }
                is GeoVisionEvent.TrackSubscribed -> {
                    addLog("VinayTrack subscribed: ${event.track.name} from ${event.participant.identity}")
                }
                is GeoVisionEvent.TrackUnsubscribed -> {
                    addLog("Vinay Track unsubscribed: ${event.track.name} from ${event.participant.identity}")
                }
                is GeoVisionEvent.ActiveSpeakersChanged -> {
                    addLog("Vinay Active speakers: ${event.speakers.joinToString { it.identity?.toString() ?: "N/A" }}")
                }
                is GeoVisionEvent.Error -> {
                    addLog("Vinay ERROR: ${event.message} ${event.exception?.localizedMessage ?: ""}")
                    if (event.message.contains("Failed to connect") || event.message.contains("Connection setup failed")) {
                        isConnecting = false
                        isConnected = false
                        connectionStatus = "Error Connecting"
                    }
                }

                is GeoVisionEvent.CustomMessageReceived -> {
                    addLog("Vinay Custom message received: ${event.message} from ${event.senderId} from ${event.isCritical} from ${event.topic}")
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("LiveKit SDK Test UI", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = livekitUrl,
            onValueChange = { livekitUrl = it },
            label = { Text("LiveKit URL") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = accessToken,
            onValueChange = { accessToken = it },
            label = { Text("Access Token") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (livekitUrl.isNotBlank() && accessToken.isNotBlank()) {
                        VisionBotSDKManager.connectToGeoVisionRoom(context, livekitUrl, accessToken)
                    } else {
                        addLog("URL or Token is empty!")
                    }
                },
                enabled = !isConnecting && !isConnected
            ) {
                Text("Connect")
            }

            Button(
                onClick = {
                    VisionBotSDKManager.disconnectFromGeoVisionRoom()
                },
                enabled = isConnected || isConnecting
            ) {
                Text("Disconnect")
            }
        }

        Text("Status: $connectionStatus")

        if (isConnected) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    coroutineScope.launch {
                        val success = VisionBotSDKManager.setCameraEnabled(!isCameraEnabledUi)
                        addLog("Set Camera to ${!isCameraEnabledUi}: SDK reported $success")
                        // Optimistic update, real update comes from TrackPublished/Unpublished event
                        // or you can refresh state after the call if SDK allows synchronous check
                        // isCameraEnabledUi = VisionBotSDKManager.isCameraEnabled() // Or wait for event
                    }
                }) {
                    Text(if (isCameraEnabledUi) "Turn Camera Off" else "Turn Camera On")
                }

                Button(onClick = {
                    coroutineScope.launch {
                        val success = VisionBotSDKManager.setMicrophoneEnabled(!isMicrophoneEnabledUi)
                        addLog("Set Mic to ${!isMicrophoneEnabledUi}: SDK reported $success")
                        // isMicrophoneEnabledUi = VisionBotSDKManager.isMicrophoneEnabled() // Or wait for event
                    }
                }) {
                    Text(if (isMicrophoneEnabledUi) "Mute Mic" else "Unmute Mic")
                }
            }
            Text("Camera: ${if (isCameraEnabledUi) "ON" else "OFF"} | Mic: ${if (isMicrophoneEnabledUi) "ON" else "OFF"}")
        }


        Text("Event Log:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 4.dp)) {
            items(eventLog.reversed()) { logEntry -> // Reversed to show newest first
                Text(logEntry, style = MaterialTheme.typography.bodySmall)
                Divider()
            }
        }
    }
}
