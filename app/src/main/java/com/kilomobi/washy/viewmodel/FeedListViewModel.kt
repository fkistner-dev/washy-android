package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.repo.FeedListRepository

class FeedListViewModel : ViewModel() {

    private val repository = FeedListRepository()

    fun getAllFeeds(): MutableLiveData<ArrayList<Feed>> {
        return feeds
    }

    private var feeds: MutableLiveData<ArrayList<Feed>>
        get() { return repository.feedList }
        set(value) { repository.feedList = value }
}