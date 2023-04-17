package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.repo.GuideListRepository

class GuideListViewModel(private val repository: GuideListRepository = GuideListRepository()) : BaseViewModel(repository) {

    fun getAllGuides(): MutableLiveData<ArrayList<Guide>> {
        return guides
    }

//    fun getGuide(id: String): MutableLiveData<Guide> {
//        repository.retrieveMerchantFeeds(id)
//        return guides.
//    }

    fun incrementGuideLike(id: String) {
        repository.incrementLike(id)
    }

    private var guides: MutableLiveData<ArrayList<Guide>>
        get() { return repository.guideList }
        set(value) { repository.guideList = value }
}