package com.kilomobi.washy.model

import com.google.firebase.Timestamp
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

data class Product(
    var title: String = "",
    var description: String = "",
    var duration: String = "",
    var price: Double = 0.0,
    var active: Boolean = true,
    var verified: Boolean = false,
    var createdAt: Timestamp = Timestamp.now(),
    var imageUrl: String = ""
) : RecyclerItem(), AdapterClick
