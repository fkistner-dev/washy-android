package com.kilomobi.washy.model

import com.google.firebase.Timestamp
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem
import java.io.Serializable

data class User(
    var userName: String = "",
    var createdAt: Timestamp = Timestamp.now(),
    var store: String = ""
) : RecyclerItem(), AdapterClick, Serializable {

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        var result = 31 * userName.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + store.hashCode()
        return result
    }
}
