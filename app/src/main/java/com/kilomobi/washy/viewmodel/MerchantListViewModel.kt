package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.repo.MerchantListRepository

class MerchantListViewModel : ViewModel() {

    private val repository = MerchantListRepository()

    fun getAllMerchants(): MutableLiveData<ArrayList<Merchant>> {
        return merchants
    }

    private var merchants: MutableLiveData<ArrayList<Merchant>>
        get() { return repository.merchantList }
        set(value) { repository.merchantList = value }
}