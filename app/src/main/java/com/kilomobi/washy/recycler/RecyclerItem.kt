package com.kilomobi.washy.recycler

interface RecyclerItem {
    val id: String?
    override fun equals(other: Any?): Boolean
}