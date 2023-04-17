package com.kilomobi.washy.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kilomobi.washy.model.Guide

class GuideListRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "guides"
        const val GUIDE_LIMIT = 20
    }

    private val db = FirebaseFirestore.getInstance()
    var guideList: MutableLiveData<ArrayList<Guide>> = MutableLiveData()

    init {
        listenGuideList()
    }

    private fun listenGuideList() {
        val mockedList: ArrayList<Guide> = ArrayList()

        var guide = Guide()
        guide.author = "Author"
        guide.steps = 1
        guide.header = "Jantes"
        guide.subHeader = "SubHeader"
        guide.text = "Produit très efficace et pratique d'utilisation, il nettoie vos jantes en profondeur et leur donne un brillant incomparable.\n" +
                "Il respecte la surface de vos jantes grâce à sa formule travaillée tout en gardant une efficacité optimale."
        guide.subText = "Subtext"
        guide.verified = true
        guide.photos = listOf("https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg")
        mockedList.add(guide)

        guide = Guide()
        guide.author = "Author"
        guide.steps = 1
        guide.header = "Vitres"
        guide.subHeader = "SubHeader"
        guide.text = "Nettoyant pour vitre et pare-brise efficace avec un séchage rapide. Ce produit est conçu pour ne pas laisser de trace."
        guide.subText = "Subtext"
        guide.verified = true
        guide.photos = listOf("https://www.shine-group.fr/668-large_default/nettoyant-vitre.jpg")
        mockedList.add(guide)

        guide = Guide()
        guide.author = "Author"
        guide.steps = 1
        guide.header = "Moquettes"
        guide.subHeader = "SubHeader"
        guide.text = "Nettoie les moquettes et tapis à base de \"mousse sèche\" pour éliminer facilement les résidus."
        guide.subText = "Subtext"
        guide.verified = true
        guide.photos = listOf("https://www.shine-group.fr/2958-large_default/renovateur-moquette-tapis.jpg")
        mockedList.add(guide)

        guide = Guide()
        guide.author = "Author"
        guide.steps = 1
        guide.header = "Goudrons et résines"
        guide.subHeader = "SubHeader"
        guide.text = "Ce décapant ne laisse ni trace, ni dépôt. Produit à séchage rapide, il viendra à bout de tous vos dépôts de goudron et résine sur votre véhicule."
        guide.subText = "Subtext"
        guide.verified = true
        guide.photos = listOf("https://www.shine-group.fr/672-large_default/decapant-goudron-resine.jpg")
        mockedList.add(guide)

        guideList.value = mockedList
        onDataReceived()
//        db.collection(COLLECTION)
//            .limit(GUIDE_LIMIT.toLong())
//            .orderBy("createdAt")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val guide = document.toObject(Guide::class.java)
//                    guide.reference = document.id
//                    mockedList.add(document.toObject(Guide::class.java))
//                }
//                guideList.value = mockedList
//                onDataReceived()
//            }
    }

    fun incrementLike(path: String) {
        db.collection(COLLECTION).document(path).update("like", ServerValue.increment(1))
    }

    // TODO ? Doubt if has utility.
//    fun retrieveGuide(id: String) {
//        var tmpGuide: Guide
//
//        db.collection(COLLECTION)
//            .whereEqualTo("guideId", id)
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    tmpGuide = (document.toObject(Guide::class.java))
//                }
//                guide.value = tmpGuide
//                onDataReceived()
//            }
//    }
}
