/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.repo.MerchantListRepository

class MerchantListViewModel(private val repository: MerchantListRepository = MerchantListRepository()) : BaseViewModel(repository) {

    fun getAllMerchants(): MutableLiveData<ArrayList<Merchant>> {
        repository.retrieveMerchantList()
        return merchants
    }

    fun getNearestMerchant(latitude: Double, longitude: Double): MutableLiveData<ArrayList<Merchant>> {
        repository.retrieveMerchant(latitude, longitude)
        return merchants
    }

    private var merchants: MutableLiveData<ArrayList<Merchant>>
        get() { return repository.merchantList }
        set(value) { repository.merchantList = value }
}