package com.kilomobi.washy.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Product
import com.kilomobi.washy.model.Rating

class MerchantRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "merchants"
        const val SUB_COLLECTION_RATINGS = "ratings"
        const val SUB_COLLECTION_PRODUCTS = "products"
        const val FIELD_TIMESTAMP = "createdAt"
    }

    private val db = FirebaseFirestore.getInstance()
    var merchant: MutableLiveData<Merchant> = MutableLiveData()
    var ratings: MutableLiveData<ArrayList<Rating>> = MutableLiveData()
    var products: MutableLiveData<ArrayList<Product>> = MutableLiveData()

    fun retrieveMerchant(merchantId: String) {
        var tmpMerchant: Merchant

        db.collection(COLLECTION)
            .document(merchantId)
            .get()
            .addOnSuccessListener { result ->
                tmpMerchant = result.toObject(Merchant::class.java)!!
                merchant.value = tmpMerchant
                onDataReceived()
            }
    }

    fun retrieveRatings(id: String) {
        val tmpRating: ArrayList<Rating> = ArrayList()

        db.collection(COLLECTION)
            .document(id)
            .collection(SUB_COLLECTION_RATINGS)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpRating.add(document.toObject(Rating::class.java))
                }
                ratings.value = tmpRating
                onDataReceived()
            }
    }

    fun retrieveProducts(id: String) {
        val tmpProduct: ArrayList<Product> = ArrayList()

        db.collection(COLLECTION)
            .document(id)
            .collection(SUB_COLLECTION_PRODUCTS)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpProduct.add(document.toObject(Product::class.java))
                }
                products.value = tmpProduct
                onDataReceived()
            }
    }

    fun addMerchant(merchant: Merchant) {
        db.collection(COLLECTION)
            .add(merchant)
            .addOnSuccessListener {
                Log.d("MerchantRepo", "DocumentSnapshot written with ID: ${it.id}")
            }
            .addOnFailureListener {
                Log.w("MerchantRepo", "Error adding document", it)
            }
    }
}
