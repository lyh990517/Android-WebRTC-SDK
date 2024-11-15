package com.example.webrtc.impl

import com.example.event.EventBus.eventFlow
import com.example.event.WebRtcEvent
import com.example.manager.WebRtcController
import com.example.manager.LocalResourceController
import com.example.model.RoomStatus
import com.example.webrtc.api.WebRtcClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class HostClient @Inject constructor(
    private val webRtcScope: CoroutineScope,
    private val eventController: EventController,
    private val webRtcController: WebRtcController,
    private val localResourceController: LocalResourceController,
    private val signalingManager: com.example.firestore.SignalingManager
) : WebRtcClient {
    override fun connect(roomID: String) {
        webRtcScope.launch {
            eventController.start()

            webRtcController.connectToPeerAsHost(roomID)

            localResourceController.startCapture()

            eventFlow.emit(WebRtcEvent.Host.SendOffer(roomID))

            signalingManager.observeSignaling(roomID)
        }
    }

    override fun toggleVoice() {
        localResourceController.toggleVoice()
    }

    override fun toggleVideo() {
        localResourceController.toggleVideo()
    }

    override suspend fun getRoomStatus(roomID: String): RoomStatus =
        signalingManager.getRoomStatus(roomID).first()

    override fun disconnect() {
        webRtcScope.cancel()
        localResourceController.dispose()
        webRtcController.closeConnection()
    }
}