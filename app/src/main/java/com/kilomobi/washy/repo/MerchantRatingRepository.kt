package com.kilomobi.washy.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kilomobi.washy.model.Merchant
import javax.inject.Inject
import javax.inject.Named
import com.kilomobi.washy.common.CompletionLiveData
import com.kilomobi.washy.common.DocumentLiveData
import com.kilomobi.washy.common.QueryLiveData
import com.kilomobi.washy.model.Rating

class MerchantRatingRepository @Inject constructor(@param:Named(COLLECTION_NAME) private val merchants: CollectionReference?) {

    companion object {
        const val COLLECTION_NAME = "merchants"
    }

    private val db = FirebaseFirestore.getInstance()

    private fun query(id: String?): Query {
        return db.collection(COLLECTION_NAME)
            .document(id!!)
            .collection("ratings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)
    }

    fun ratings(id: String?): QueryLiveData<Rating>? {
        return QueryLiveData(
            query(id),
            Rating::class.java
        )
    }

    fun merchant(id: String?): DocumentLiveData<Merchant>? {
        if (id == null) {
            return null
        }
        val merchantRef =
            merchants!!.document(id)
        val data: DocumentLiveData<Merchant> =
            DocumentLiveData(
                merchantRef,
                Merchant::class.java
            )
        merchantRef.addSnapshotListener(data)
        return data
    }

    fun addRating(merchantId: String?, rating: Rating?): CompletionLiveData {
        val completion = CompletionLiveData()

        merchants?.firestore?.runTransaction {
            val merchantRef = merchants.document()
            val ratingRef = merchants.document().collection("ratings").document()

            val merchant: Merchant? = it[merchantRef].toObject(
                Merchant::class.java
            )

            // Compute new number of ratings
            val newNumRatings: Int = merchant!!.numRating.plus(1)

            // Compute new average rating
            val oldRatingTotal: Float = merchant.avgRating * merchant.numRating
            val newAvgRating: Float =
                (oldRatingTotal + rating!!.stars) / newNumRatings

            // Set new merchant info
            merchant.numRating = newNumRatings
            merchant.avgRating = newAvgRating.toFloat()

            // Commit to Firestore
            it[merchantRef] = merchant
            it[ratingRef] = rating
        }
        return completion
    }
}