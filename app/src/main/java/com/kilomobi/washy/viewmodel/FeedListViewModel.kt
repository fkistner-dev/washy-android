/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

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