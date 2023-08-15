package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kilomobi.washy.model.Feed

class FeedListRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "feeds"
        const val FEED_HOME_LIMIT = 3
        const val FEED_MERCHANT_LIMIT = 20
    }

    private val db = FirebaseFirestore.getInstance()
    var feedList: MutableLiveData<ArrayList<Feed>> = MutableLiveData()
    var merchantFeedList: MutableLiveData<ArrayList<Feed>> = MutableLiveData()

    init {
        listenFeedList()
    }

    private fun listenFeedList() {
        val tmpListFeed: ArrayList<Feed> = ArrayList()

        db.collection(COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(FEED_HOME_LIMIT.toLong())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val feed = document.toObject(Feed::class.java)
                    feed.reference = document.id
                    tmpListFeed.add(document.toObject(Feed::class.java))
                }
                feedList.value = tmpListFeed
                onDataReceived()
            }
    }

    fun incrementLike(path: String) {
        db.collection(COLLECTION).document(path).update("like", ServerValue.increment(1))
    }

    fun retrieveMerchantFeeds(id: String) {
        val tmpListFeed: ArrayList<Feed> = ArrayList()

        db.collection(COLLECTION)
            .whereEqualTo("merchantId", id)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(FEED_MERCHANT_LIMIT.toLong())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpListFeed.add(document.toObject(Feed::class.java))
                }
                merchantFeedList.value = tmpListFeed
                onDataReceived()
            }
    }
}
