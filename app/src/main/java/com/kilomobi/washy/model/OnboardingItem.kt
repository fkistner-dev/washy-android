package com.kilomobi.washy.model

import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

data class OnboardingItem(val image: Int? = null, val background: Int, val title: String, val description: String = "") : RecyclerItem(),
    AdapterClick {
    override fun equals(other: Any?): Boolean {
        return false
    }
}