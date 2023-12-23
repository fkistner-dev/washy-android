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
import com.kilomobi.washy.model.User

class UserRepository : BaseRepository() {
    companion object {
        const val COLLECTION = "users"
    }

    private val db = FirebaseFirestore.getInstance()
    var user: MutableLiveData<User?> = MutableLiveData()

    fun addUser(user: User, documentId: String) {
        db.collection(COLLECTION).document(documentId).set(user)
    }

    fun getUser(documentId: String) {
        var tmpUser: User? = null

        db.collection(COLLECTION).document(documentId)
            .get()
            .addOnSuccessListener { result ->
                if (!result.data.isNullOrEmpty()) {
                    tmpUser = result.toObject(User::class.java)!!
                }

                user.value = tmpUser
            }
    }
}