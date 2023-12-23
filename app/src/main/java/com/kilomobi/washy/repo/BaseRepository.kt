/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData

abstract class BaseRepository {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun onDataReceived() {
        isLoading.value = false
    }
}