package com.kilomobi.washy.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.kilomobi.washy.model.User

class UserRepository : BaseRepository() {
    companion object {
        const val COLLECTION = "users"
    }

    private val db = FirebaseFirestore.getInstance()
    var user: MutableLiveData<User> = MutableLiveData()

    fun addUser(user: User, documentId: String) {
        db.collection(COLLECTION).document(documentId)
            .set(user)
            .addOnSuccessListener {
                Log.d("UserRepo", "User written with success")
            }
            .addOnFailureListener {
                Log.w("UserRepo", "Error adding document", it)
            }
    }

    fun getUser(documentId: String) {
        var tmpUser: User

        // TODO : Check why it doesn't response
        db.collection(MerchantRepository.COLLECTION).document(documentId)
            .get()
            .addOnSuccessListener { result ->
                if (!result.data.isNullOrEmpty()) {
                    tmpUser = result.toObject(User::class.java)!!
                    user.value = tmpUser
                    onDataReceived()
                }
            }
            .addOnFailureListener {
                Log.w("UserRepo", "Error adding document", it)
            }
    }
}