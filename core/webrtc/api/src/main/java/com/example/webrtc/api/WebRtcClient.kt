package com.example.webrtc.api

import com.example.model.RoomStatus

interface WebRtcClient {
    suspend fun getRoomStatus(roomID: String): RoomStatus

    fun toggleVoice()

    fun toggleVideo()

    fun disconnect()

    fun connect(roomID: String)
}
