package com.kilomobi.washy.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kilomobi.washy.common.CompletionLiveData
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
    var ref: MutableLiveData<String> = MutableLiveData()

    fun retrieveMerchant(merchantId: String) {
        var tmpMerchant: Merchant

        db.collection(COLLECTION)
            .document(merchantId)
            .get()
            .addOnSuccessListener { result ->
                tmpMerchant = result.toObject(Merchant::class.java)!!
                tmpMerchant.reference = result.id
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
                    val rating = document.toObject(Rating::class.java)
                    rating.reference = document.id
                    tmpRating.add(rating)
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

    fun addMerchant(merchant: Merchant): MutableLiveData<String> {
        db.collection(COLLECTION)
            .add(merchant)
            .addOnSuccessListener {
                ref.value = it.id
                onDataReceived()
                Log.d("MerchantRepo", "DocumentSnapshot written with ID: $it.id")
            }
            .addOnFailureListener {
                Log.w("MerchantRepo", "Error adding document", it)
            }

        return ref
    }

    fun addRating(merchantId: String, rating: Rating): CompletionLiveData {
        val completion = CompletionLiveData()

        db.collection(MerchantListRepository.COLLECTION).firestore.runTransaction {
            val collectionRef = db.collection(MerchantListRepository.COLLECTION)
            val merchantRef = collectionRef.document(merchantId)
            val ratingRef = merchantRef.collection(SUB_COLLECTION_RATINGS).document()

            val merchant: Merchant? = it[merchantRef].toObject(
                Merchant::class.java
            )

            // Compute new number of ratings
            val newNumRatings: Int = merchant!!.numRating.plus(1)

            // Compute new average rating
            val oldRatingTotal: Float = merchant.avgRating * merchant.numRating
            val newAvgRating: Float = (oldRatingTotal + rating.stars) / newNumRatings

            // Set new merchant info
            merchant.numRating = newNumRatings
            merchant.avgRating = newAvgRating

            // Commit to Firestore
            it[merchantRef] = merchant
            it[ratingRef] = rating
        }

        return completion
    }

    fun modifyRating(merchantId: String, rating: Rating) {
        val ratingRef = db.collection(COLLECTION)
            .document(merchantId)
            .collection(SUB_COLLECTION_RATINGS).document(rating.reference)

        rating.editedAt = Timestamp.now()
        ratingRef.set(rating)
    }

    fun deleteRating(merchantId: String, rating: Rating) {
        val ratingRef = db.collection(COLLECTION)
            .document(merchantId)
            .collection(SUB_COLLECTION_RATINGS).document(rating.reference)
        ratingRef.delete()
    }
}