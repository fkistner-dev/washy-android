/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.kilomobi.washy.model.Guide

class GuideListRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "guides"
        const val GUIDE_LIMIT = 20
    }

    private val db = FirebaseFirestore.getInstance()
    var guideList: MutableLiveData<ArrayList<Guide>> = MutableLiveData()
    var guide: MutableLiveData<Guide> = MutableLiveData()

    init {
        listenGuideList()
    }

    private fun listenGuideList() {
        val tmpGuideList = ArrayList<Guide>()

        db.collection(COLLECTION)
            .limit(GUIDE_LIMIT.toLong())
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val guide = document.toObject(Guide::class.java)
                    guide.reference = document.id
                    tmpGuideList.add(guide)
                }
                guideList.value = tmpGuideList
                onDataReceived()
            }
    }

    fun retrieveGuide(id: String) {
        db.collection(COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                guide.value = result.toObject(Guide::class.java)
                onDataReceived()
            }
    }
}
