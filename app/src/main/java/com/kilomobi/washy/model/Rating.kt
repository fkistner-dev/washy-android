/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem
import java.io.Serializable

data class Rating(
    @Exclude var reference: String = "",
    var userId: String = "",
    var userName: String = "",
    var createdAt: Timestamp = Timestamp.now(),
    var editedAt: Timestamp? = null,
    var stars: Float = 0f,
    var verified: Boolean = false,
    var text: String? = null,
    var language: String = "fr") : RecyclerItem(), AdapterClick, Serializable {

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + stars.hashCode()
        result = 31 * result + verified.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        return result
    }
}
