package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.kilomobi.washy.model.Merchant

class MerchantListRepository {

    companion object {
        const val COLLECTION = "merchants"
    }

    private val db = FirebaseFirestore.getInstance()
    var merchantList: MutableLiveData<ArrayList<Merchant>> = MutableLiveData<ArrayList<Merchant>>()

    init {
        listenMerchantList()
    }

    private fun listenMerchantList() {
        val tmpListMerchant: ArrayList<Merchant> = ArrayList()

        db.collection(COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpListMerchant.add(document.toObject(Merchant::class.java))
                }
                merchantList.value = tmpListMerchant
            }
    }
}