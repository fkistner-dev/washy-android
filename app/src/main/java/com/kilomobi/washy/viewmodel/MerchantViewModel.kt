package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Product
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.repo.MerchantRepository

class MerchantViewModel(private val repository: MerchantRepository = MerchantRepository()) : BaseViewModel(repository) {

    fun getMerchant(id: String): MutableLiveData<Merchant>? {
        repository.retrieveMerchant(id)
        return merchant
    }

    fun getRatings(id: String): MutableLiveData<ArrayList<Rating>> {
        repository.retrieveRatings(id)
        return ratings
    }

    fun getProducts(id: String): MutableLiveData<ArrayList<Product>> {
        repository.retrieveProducts(id)
        return products
    }

    fun addMerchant(merchant: Merchant): MutableLiveData<String> {
        repository.addMerchant(merchant)
        return ref
    }

    fun addRating(merchantId: String, rating: Rating) {
        repository.addRating(merchantId, rating)
    }

    fun modifyRating(merchantId: String, rating: Rating) {
        repository.modifyRating(merchantId, rating)
    }

    fun deleteRating(merchantId: String, rating: Rating) {
        repository.deleteRating(merchantId, rating)
    }

    private var merchant: MutableLiveData<Merchant>
        get() { return repository.merchant }
        set(value) { repository.merchant = value }

    private var ratings: MutableLiveData<ArrayList<Rating>>
        get() { return repository.ratings }
        set(value) { repository.ratings = value }

    private var products: MutableLiveData<ArrayList<Product>>
        get() { return repository.products }
        set(value) { repository.products = value }

    private var ref: MutableLiveData<String>
        get() { return repository.ref }
        set(value) { repository.ref = value }
}