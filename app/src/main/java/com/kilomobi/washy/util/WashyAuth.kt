package com.kilomobi.washy.util

import java.util.UUID

object WashyAuth {
    private var uid: String? = null

    fun getUid(): String? {
        return uid
    }

    fun generateUid(): String {
        uid = UUID.randomUUID().toString()
        return uid as String
    }

    fun resetUid() {
        uid = null
    }
}