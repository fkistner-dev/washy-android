package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kilomobi.washy.model.Feed

class FeedListRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "feeds"
        const val FEED_LIMIT = 20
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
            .limit(FEED_LIMIT.toLong())
            .orderBy("createdAt")
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
            .limit(FEED_LIMIT.toLong())
            .orderBy("createdAt")
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
