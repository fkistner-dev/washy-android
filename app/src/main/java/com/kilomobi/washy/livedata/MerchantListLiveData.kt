package com.kilomobi.washy.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import com.kilomobi.washy.model.Merchant

class MerchantListLiveData : LiveData<List<Merchant>>() {

    private val merchantListTemp: ArrayList<Merchant> = ArrayList()
    var merchantList: MutableLiveData<List<Merchant>> = MutableLiveData<List<Merchant>>()
    private var listenerRegistration = ListenerRegistration {}

//
//    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
//        // 2
//        if (p0 != null && !p0.isEmpty) {
//            // 3
//            val documents: List<DocumentSnapshot>? = p0.documents
////            val merchantListItems: MutableMap<String, Any>? = documents.get(0)
//            // 4
//            merchantListTemp.clear()
//            // 5
////            if (merchantListItems != null) {
////                for ((_, value) in merchantListItems) {
////                    if (value is Merchant) {
////                        val itemToAdd = Merchant(value.uid,
////                        value.name,
////                        value.presentation,
////                        value.position,
////                        value.priceStart,
////                        value.avgRating,
////                        value.numRating,
////                        value.services,
////                        value.adminId)
////                        merchantListTemp.add(itemToAdd)
////                    }
////                }
////            }
//            // 6
//            merchantList.setValue(merchantListTemp)
//        } else {
//            Log.d(TAG, "error")
//        }
//    }
}