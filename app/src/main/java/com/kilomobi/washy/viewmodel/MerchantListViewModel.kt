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