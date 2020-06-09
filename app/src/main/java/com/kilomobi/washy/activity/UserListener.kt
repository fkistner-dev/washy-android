package com.kilomobi.washy.activity

import com.google.firebase.auth.FirebaseUser

interface UserListener {
    fun onConnected(user: FirebaseUser)
}