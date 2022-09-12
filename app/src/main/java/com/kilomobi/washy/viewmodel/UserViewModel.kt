package com.kilomobi.washy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kilomobi.washy.model.User
import com.kilomobi.washy.repo.UserRepository

class UserViewModel(private val repository: UserRepository = UserRepository()) : BaseViewModel(repository) {

    fun addUser(user: User, documentId: String) {
        repository.addUser(user, documentId)
    }

    fun getUser(documentId: String): MutableLiveData<User?> {
        repository.getUser(documentId)
        return user
    }

    private var user: MutableLiveData<User?>
        get() { return repository.user }
        set(value) { repository.user = value }
}