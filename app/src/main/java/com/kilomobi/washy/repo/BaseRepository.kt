package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData

abstract class BaseRepository {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun onDataReceived() {
        isLoading.value = false
    }
}