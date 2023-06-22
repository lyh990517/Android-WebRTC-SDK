package com.example.webrtc.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import org.webrtc.IceCandidate
import javax.inject.Inject

class WebRTCRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : WebRTCRepository {
    override fun sendIceCandidate(candidate: IceCandidate?, isJoin: Boolean, roomId: String) {
        val type = if (isJoin) "answerCandidate" else "offerCandidate"

        val iceCandidate = hashMapOf(
            "serverUrl" to candidate?.serverUrl,
            "sdpMid" to candidate?.sdpMid,
            "sdpMLineIndex" to candidate?.sdpMLineIndex,
            "sdpCandidate" to candidate?.sdp,
            "type" to type
        )

        firestore.collection("calls").document(roomId).collection("candidates").document(type)
            .set(iceCandidate).addOnSuccessListener {
                Log.e("Rsupport", "sendIceCandidate: Success")
            }.addOnFailureListener {
                Log.e("Rsupport", "sendIceCandidate: Error $it")
            }
    }
}