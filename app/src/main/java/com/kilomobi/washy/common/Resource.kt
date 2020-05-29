package com.kilomobi.washy.common

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class Resource<T> private constructor(
    @field:Nullable @param:Nullable private val data: T?,
    @field:Nullable @param:Nullable private val error: Exception?
) {

    constructor(@NonNull data: T) : this(data, null) {}
    constructor(@NonNull exception: Exception?) : this(null, exception) {}

    val isSuccessful: Boolean
        get() = data != null && error == null

    @NonNull
    fun data(): T? {
        check(error == null) { "error is not null. Call isSuccessful() first." }
        return data
    }

    @NonNull
    fun error(): Exception? {
        check(data == null) { "data is not null. Call isSuccessful() first." }
        return error
    }

}