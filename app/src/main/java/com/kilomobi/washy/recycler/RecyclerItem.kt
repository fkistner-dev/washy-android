package com.kilomobi.washy.recycler

import androidx.annotation.NonNull
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
abstract class RecyclerItem {
    abstract override fun equals(other: Any?): Boolean

    fun <T> withId(@NonNull id: Long): T {
        return this as T
    }
}