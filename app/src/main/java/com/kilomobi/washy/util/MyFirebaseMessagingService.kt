package com.kilomobi.washy.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //a) What's the life cycle of the service? How can I ensure this method is getting called?
        //b) If the service is killed at some point, will system restart it on receiving new messages?
        Log.d("HELLONOTIF", "From: " + remoteMessage.from)
    }
}