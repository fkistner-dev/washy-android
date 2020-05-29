package com.kilomobi.washy.model

import com.google.firebase.Timestamp
import com.kilomobi.washy.recycler.RecyclerItem

data class Product(
    var title: String = "",
    var description: String = "",
    var duration: String = "",
    var price: Double = 0.0,
    var isActive: Boolean = true,
    var createdAt: Timestamp = Timestamp.now(),
    var imageUrl: String = ""
) : RecyclerItem()