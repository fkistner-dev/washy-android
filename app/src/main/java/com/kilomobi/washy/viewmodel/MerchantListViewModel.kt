package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.repo.MerchantListRepository

class MerchantListViewModel(private val repository: MerchantListRepository = MerchantListRepository()) : BaseViewModel(repository) {

    fun getAllMerchants(): MutableLiveData<ArrayList<Merchant>> {
        return merchants
    }

    private var merchants: MutableLiveData<ArrayList<Merchant>>
        get() { return repository.merchantList }
        set(value) { repository.merchantList = value }
}