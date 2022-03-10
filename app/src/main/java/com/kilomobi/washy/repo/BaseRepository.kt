package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData

abstract class BaseRepository {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun onDataReceived() {
        isLoading.value = false
    }
}