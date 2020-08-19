package com.kilomobi.washy.model

import com.google.firebase.Timestamp
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

data class Rating(
    var userId: String = "",
    var userName: String = "",
    var createdAt: Timestamp = Timestamp.now(),
    var rating: Float = 0f,
    var verified: Boolean = false,
    var text: String? = null) : RecyclerItem(), AdapterClick {

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }
}
