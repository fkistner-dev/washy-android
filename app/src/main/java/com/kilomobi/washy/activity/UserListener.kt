package com.kilomobi.washy.activity

interface UserListener {
    fun onAuthenticationConnected(user: String)
    fun onAuthenticationCancel()
}