package com.kilomobi.washy.model

import com.google.firebase.Timestamp
import com.kilomobi.washy.recycler.RecyclerItem

data class Rating(
    var userId: String = "",
    var userName: String = "",
    var createdAt: Timestamp = Timestamp.now(),
    var rating: Double = 0.0,
    var text: String? = null) : RecyclerItem() {

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }
}
