package com.kilomobi.washy.common

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import com.kilomobi.washy.recycler.RecyclerItem

class QueryLiveData<T : RecyclerItem?>(private val query: Query, private val type: Class<T>) :
    LiveData<Resource<List<T>?>?>(), EventListener<QuerySnapshot?> {

    private var registration: ListenerRegistration? = null
    override fun onEvent(snapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            value = Resource(e)
            return
        }
        value =
            Resource(snapshots?.let { documentToList(it) })
    }

    override fun onActive() {
        super.onActive()
        registration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        if (registration != null) {
            registration!!.remove()
            registration = null
        }
    }

    @NonNull
    private fun documentToList(snapshots: QuerySnapshot): List<T> {
        val retList: MutableList<T> = ArrayList()
        if (snapshots.isEmpty) {
            return retList
        }
        for (document in snapshots.documents) {
            retList.add(document.toObject(type)!!.withId(document.id.toLong()))
        }
        return retList
    }
}