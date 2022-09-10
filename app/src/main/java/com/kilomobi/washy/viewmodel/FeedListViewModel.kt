package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.repo.FeedListRepository

class FeedListViewModel(private val repository: FeedListRepository = FeedListRepository()) : BaseViewModel(repository) {

    fun getAllFeeds(): MutableLiveData<ArrayList<Feed>> {
        return feeds
    }

    fun getMerchantFeed(id: String): MutableLiveData<ArrayList<Feed>> {
        repository.retrieveMerchantFeeds(id)
        return merchantFeeds
    }

    fun incrementFeedLike(id: String) {
        repository.incrementLike(id)
    }

    private var feeds: MutableLiveData<ArrayList<Feed>>
        get() { return repository.feedList }
        set(value) { repository.feedList = value }

    private var merchantFeeds: MutableLiveData<ArrayList<Feed>>
        get() { return repository.merchantFeedList }
        set(value) { repository.merchantFeedList = value }
}