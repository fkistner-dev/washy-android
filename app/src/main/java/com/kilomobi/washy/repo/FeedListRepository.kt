package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.kilomobi.washy.model.Feed

class FeedListRepository {

    companion object {
        const val COLLECTION = "feeds"
        const val FEED_LIMIT = 20
    }

    private val db = FirebaseFirestore.getInstance()
    var feedList: MutableLiveData<ArrayList<Feed>> = MutableLiveData<ArrayList<Feed>>()
    var merchantFeedList: MutableLiveData<ArrayList<Feed>> = MutableLiveData<ArrayList<Feed>>()

    init {
        listenFeedList()
    }

    private fun listenFeedList() {
        val tmpListFeed: ArrayList<Feed> = ArrayList()

        db.collection(COLLECTION)
            .limit(FEED_LIMIT.toLong())
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpListFeed.add(document.toObject(Feed::class.java))
                }
                feedList.value = tmpListFeed
            }
    }

    fun retrieveMerchantFeeds(id: String) {
        val tmpListFeed: ArrayList<Feed> = ArrayList()

        db.collection(COLLECTION)
            .whereEqualTo("merchantId", id)
            .limit(FEED_LIMIT.toLong())
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpListFeed.add(document.toObject(Feed::class.java))
                }
                merchantFeedList.value = tmpListFeed
            }
    }
}
