package com.example.data.client

import com.example.domain.RemoteSurface
import com.example.domain.repository.FireStoreRepository
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeerConnectionManager @Inject constructor(
    @RemoteSurface private val remoteSurface: SurfaceViewRenderer,
    private val fireStoreRepository: FireStoreRepository,
    private val peerConnectionFactory: PeerConnectionFactory,
    private val localMediaStream: MediaStream,
) {
    private val iceServer =
        listOf(PeerConnection.IceServer.builder(ICE_SERVER_URL).createIceServer())

    private lateinit var peerConnection: PeerConnection

    fun connectToPeer(isHost: Boolean, roomID: String) {
        peerConnectionFactory.createPeerConnection(
            iceServer,
            createPeerConnectionObserver(isHost, roomID)
        )?.let { connection ->
            peerConnection = connection
        }
    }

    fun addStream() {
        peerConnection.addStream(localMediaStream)
    }

    fun closeConnection() {
        peerConnection.close()
    }

    fun getPeerConnection() = peerConnection

    private fun createPeerConnectionObserver(
        isHost: Boolean,
        roomID: String,
    ): PeerConnection.Observer =
        object : PeerConnection.Observer {
            override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
            override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
            override fun onIceConnectionReceivingChange(p0: Boolean) {}
            override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
            override fun onIceCandidate(p0: IceCandidate?) {
                p0?.let {
                    fireStoreRepository.sendIceCandidateToRoom(it, isHost, roomID)
                    peerConnection.addIceCandidate(it)
                }
            }

            override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
            override fun onAddStream(p0: MediaStream?) {
                p0?.let { mediaStream ->
                    mediaStream.videoTracks?.get(0)?.addSink(remoteSurface)
                }
            }

            override fun onRemoveStream(p0: MediaStream?) {}
            override fun onDataChannel(p0: DataChannel?) {}
            override fun onRenegotiationNeeded() {}
            override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {}
        }

    companion object {
        private const val ICE_SERVER_URL = "stun:stun4.l.google.com:19302"
    }
}
