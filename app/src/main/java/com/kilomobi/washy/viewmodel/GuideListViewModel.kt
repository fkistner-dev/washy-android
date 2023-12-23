/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.repo.GuideListRepository

class GuideListViewModel(private val repository: GuideListRepository = GuideListRepository()) : BaseViewModel(repository) {

    fun getAllGuides(): MutableLiveData<ArrayList<Guide>> {
        return guideList
    }

    fun getGuide(id: String): MutableLiveData<Guide> {
        repository.retrieveGuide(id)
        return guide
    }

    private var guideList: MutableLiveData<ArrayList<Guide>>
        get() { return repository.guideList }
        set(value) { repository.guideList = value }

    private var guide: MutableLiveData<Guide>
        get() { return repository.guide }
        set(value) { repository.guide = value }
}