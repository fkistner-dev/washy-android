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
//        val mockedList: ArrayList<Guide> = ArrayList()
//        var mockGuide = Guide()
//        mockGuide.author = "Author"
//        mockGuide.steps = 1
//        mockGuide.title = "Jantes"
//        mockGuide.description = "Produit très efficace et pratique d'utilisation, il nettoie vos jantes en profondeur et leur donne un brillant incomparable.\n" +
//                "Il respecte la surface de vos jantes grâce à sa formule travaillée tout en gardant une efficacité optimale."
//        var innerGuide = InnerGuide()
//        innerGuide.subHeaders = listOf("subHeader1", "subHeader2", "subHeader3")
//        innerGuide.steps = listOf("step1", "step2", "step3")
//        innerGuide.subTexts = listOf("subtext1", "subtext2", "subtext3")
//        innerGuide.photos = listOf("https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg", "https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg", "https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg")
//        var innerGuide2 = InnerGuide()
//        innerGuide2.subHeaders = listOf("subHeader1", "subHeader2", "subHeader3")
//        innerGuide2.steps = listOf("step1", "step2", "step3")
//        innerGuide2.subTexts = listOf("subtext1", "subtext2", "subtext3")
//        innerGuide2.photos = listOf("https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg", "https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg", "https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg")
//        mockGuide.verified = true
//        mockGuide.photo = "https://www.shine-group.fr/673-large_default/nettoyant-jantes-hard.jpg"
//        mockedList.add(mockGuide)
//
//        mockGuide = Guide()
//        mockGuide.author = "Author"
//        mockGuide.steps = 1
//        mockGuide.title = "Vitres"
//        mockGuide.description = "Nettoyant pour vitre et pare-brise efficace avec un séchage rapide. Ce produit est conçu pour ne pas laisser de trace."
//        mockGuide.verified = true
//        mockGuide.photo = "https://www.shine-group.fr/668-large_default/nettoyant-vitre.jpg"
//        mockedList.add(mockGuide)
//
//        mockGuide = Guide()
//        mockGuide.author = "Author"
//        mockGuide.steps = 1
//        mockGuide.title = "Moquettes"
//        mockGuide.description = "Nettoie les moquettes et tapis à base de \"mousse sèche\" pour éliminer facilement les résidus."
//        mockGuide.verified = true
//        mockGuide.photo = "https://www.shine-group.fr/2958-large_default/renovateur-moquette-tapis.jpg"
//        mockedList.add(mockGuide)
//
//        mockGuide = Guide()
//        mockGuide.author = "Author"
//        mockGuide.steps = 1
//        mockGuide.title = "Goudrons et résines"
//        mockGuide.description = "Ce décapant ne laisse ni trace, ni dépôt. Produit à séchage rapide, il viendra à bout de tous vos dépôts de goudron et résine sur votre véhicule."
//        mockGuide.verified = true
//        mockGuide.photo = "https://www.shine-group.fr/672-large_default/decapant-goudron-resine.jpg"
//        mockedList.add(mockGuide)
//        mockGuideList.value = mockedList
//        onDataReceived()
        val tmpGuideList = ArrayList<Guide>()
//
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
        var tmpGuide = Guide()

        db.collection(COLLECTION)
            .whereEqualTo("mockGuideId", id)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpGuide = (document.toObject(Guide::class.java))
                }
                guide.value = tmpGuide
                onDataReceived()
            }
    }
}
