/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.repo.FeedRepository

class FeedViewModel(private val repository: FeedRepository = FeedRepository()) : BaseViewModel(repository) {

    fun getMerchantFeed(id: String): MutableLiveData<ArrayList<Feed>>? {
        repository.retrieveMerchantFeed(id)
        return feeds
    }

    private var feeds: MutableLiveData<ArrayList<Feed>>
        get() { return repository.feedList }
        set(value) { repository.feedList = value }
}