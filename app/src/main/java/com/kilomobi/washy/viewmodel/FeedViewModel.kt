package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.repo.FeedRepository

class FeedViewModel : ViewModel() {

    private val repository = FeedRepository()

    fun getMerchantFeed(id: String): MutableLiveData<ArrayList<Feed>>? {
        repository.retrieveMerchantFeed(id)
        return feeds
    }

    private var feeds: MutableLiveData<ArrayList<Feed>>
        get() { return repository.feedList }
        set(value) { repository.feedList = value }
}