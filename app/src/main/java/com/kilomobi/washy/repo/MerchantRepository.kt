/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kilomobi.washy.common.CompletionLiveData
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Product
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.model.SimpleRating
import kotlin.random.Random

class MerchantRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "merchants"
        const val SUB_COLLECTION_RATINGS = "ratings"
        const val SUB_COLLECTION_PRODUCTS = "products"
        const val FIELD_TIMESTAMP = "createdAt"
    }

    private val db = FirebaseFirestore.getInstance()
    var merchant: MutableLiveData<Merchant> = MutableLiveData()
    var ratings: MutableLiveData<ArrayList<Rating>> = MutableLiveData()
    var products: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    var ref: MutableLiveData<String> = MutableLiveData()

    private val feedArray: List<SimpleRating> = listOf(
        SimpleRating(5f, "Service rapide et efficace. Mon véhicule brille comme neuf à chaque visite."),
        SimpleRating(4.5f, "Personnel aimable. Le lavage est bien fait, mais parfois des traces d'eau persistent."),
        SimpleRating(3.5f, "Prix raisonnable, mais la qualité du lavage pourrait être améliorée."),
        SimpleRating(4f, "Bonne expérience globale. Les employés sont professionnels."),
        SimpleRating(5f, "Je suis un client fidèle. Le lavage est toujours à la hauteur de mes attentes."),
        SimpleRating(3f, "Des retards parfois, mais le travail est correct."),
        SimpleRating(4.5f, "Excellent service client. Le lavage intérieur est soigné."),
        SimpleRating(4f, "Les tarifs sont justes. Les résultats sont bons."),
        SimpleRating(5f, "Rien à redire, je recommande ce service de lavage."),
        SimpleRating(3.5f, "Le lavage extérieur est bien, mais l'intérieur pourrait être plus minutieux."),
        SimpleRating(4f, "Bon rapport qualité-prix. Le lavage est rapide et efficace."),
        SimpleRating(4.5f, "J'ai été agréablement surpris. La qualité était meilleure que ce à quoi je m'attendais."),
        SimpleRating(3.0f, "Très satisfait du lavage extérieur. L'intérieur était correct."),
        SimpleRating(4.0f, "Service solide. Les résultats étaient au-dessus de la moyenne."),
        SimpleRating(4.2f, "Assez impressionné. Ils ont fait un bon travail sur les taches difficiles."),
        SimpleRating(4.5f, "Je reviendrai. Le service global était bon."),
        SimpleRating(5f, "J'ai eu un excellent service à chaque visite. Les résultats sont toujours constants."),
        SimpleRating(5f, "Le personnel est attentif aux détails. Je suis très satisfait."),
        SimpleRating(4.9f, "Un excellent service client et des résultats de lavage exceptionnels."),
        SimpleRating(4.8f, "La qualité du lavage est remarquable. Ils ont pris soin des petites imperfections."),
        SimpleRating(4.9f, "Chaque fois que je vais là-bas, je suis étonné de la qualité du travail. Les employés sont vraiment professionnels."),
        SimpleRating(5.0f, "Des résultats exceptionnels. Mon véhicule est comme neuf après chaque visite."),
        SimpleRating(4.9f, "Je recommande vivement ce service. Ils vont au-delà des attentes."),
        SimpleRating(4.8f, "Une expérience fantastique à chaque fois. Le lavage intérieur est impeccable."),
        SimpleRating(5.0f, "Le lavage extérieur et intérieur était impeccable. Service exceptionnel."),
        SimpleRating(4.7f, "Ils ont pris soin des détails que d'autres services ont négligés. Très impressionné."),
        SimpleRating(4.8f, "La propreté de mon véhicule après le lavage est incomparable. Le meilleur service."),
        SimpleRating(4.9f, "L'équipe fait un travail exceptionnel. Mon véhicule n'a jamais été aussi propre."),
        SimpleRating(4.8f, "La qualité est toujours au rendez-vous. Ils maintiennent des normes élevées."),
        SimpleRating(5.0f, "Des résultats de lavage exceptionnels. Je suis un client fidèle."),
        SimpleRating(4.9f, "Le lavage intérieur était incroyable. Ils ont même nettoyé les taches difficiles."),
        SimpleRating(4.9f, "Service client impeccable et résultats de lavage de première classe."),
        SimpleRating(4.8f, "La qualité est constante. Je recommande vivement ce service de lavage auto."),
        SimpleRating(4.9f, "Ils vont au-delà de mes attentes à chaque visite. Service de première classe."),
        SimpleRating(2.0f, "Le lavage extérieur était médiocre. Des traces étaient toujours visibles."),
        SimpleRating(1.5f, "Le service était lent et la qualité du lavage n'était pas à la hauteur."),
        SimpleRating(2.0f, "Des zones ont été négligées lors du lavage. Décevant."),
        SimpleRating(1.8f, "Le personnel n'était pas très attentif. Mon véhicule est sorti avec des taches."),
        SimpleRating(1.0f, "Mauvaise expérience. Mon véhicule est ressorti sale."),
        SimpleRating(1.2f, "Le lavage intérieur était bâclé. Des saletés étaient encore présentes."),
        SimpleRating(2.0f, "Aucun effort pour nettoyer les coins difficiles à atteindre. Service insatisfaisant."),
        SimpleRating(1.5f, "La propreté intérieure était loin d'être satisfaisante. Des débris étaient encore présents."),
        SimpleRating(1.0f, "Je ne recommande pas ce service. La qualité n'est pas à la hauteur du prix."),
        SimpleRating(1.2f, "Le lavage extérieur était tellement basique. Rien d'extraordinaire.")
    )

    private val firstNames = listOf(
        "Emma", "Louis", "Jade", "Gabriel", "Léa", "Raphaël", "Chloé", "Jules", "Zoé", "Lucas",
        "Manon", "Hugo", "Inès", "Adam", "Camille", "Clara", "Liam", "Lou", "Nathan", "Lola",
        "Arthur", "Rose", "Théo", "Léna", "Maël", "Mila", "Ethan", "Ambre", "Paul", "Juliette",
        "Gabin", "Anna", "Maxime", "Sarah", "Louis", "Eva", "Tom", "Lina", "Nathan", "Charlotte",
        "Noah", "Manon", "Lucas", "Alice", "Eliott", "Lisa", "Samuel", "Emma", "Baptiste", "Léonie"
    )


    fun retrieveMerchant(merchantId: String) {
        var tmpMerchant: Merchant

        db.collection(COLLECTION)
            .document(merchantId)
            .get()
            .addOnSuccessListener { result ->
                tmpMerchant = result.toObject(Merchant::class.java)!!
                tmpMerchant.reference = result.id
                merchant.value = tmpMerchant
                onDataReceived()
            }
    }

    fun retrieveRatings(id: String) {
        val tmpListFeed: ArrayList<Rating> = ArrayList()

        val feedNumber = Random.nextInt(1, 6)
        // Shake the list
        val shuffledArray = feedArray.shuffled()
        val selectedFeeds = shuffledArray.subList(0, minOf(feedNumber, feedArray.size))

        for (feed in selectedFeeds) {
            val rating = Rating(text = feed.text, userName = firstNames[Random.nextInt(0, 49)], verified = true, stars = feed.stars)
            tmpListFeed.add(rating)
        }

        ratings.value = tmpListFeed
        onDataReceived()
    }

    fun retrieveProducts(id: String) {
        val tmpProduct: ArrayList<Product> = ArrayList()

        db.collection(COLLECTION)
            .document(id)
            .collection(SUB_COLLECTION_PRODUCTS)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpProduct.add(document.toObject(Product::class.java))
                }
                products.value = tmpProduct
                onDataReceived()
            }
    }

    fun addMerchant(merchant: Merchant): MutableLiveData<String> {
        db.collection(COLLECTION)
            .add(merchant)
            .addOnSuccessListener {
                ref.value = it.id
                onDataReceived()
                Log.d("MerchantRepo", "DocumentSnapshot written with ID: $it.id")
            }
            .addOnFailureListener {
                Log.w("MerchantRepo", "Error adding document", it)
            }

        return ref
    }

    fun addRating(merchantId: String, rating: Rating): CompletionLiveData {
        val completion = CompletionLiveData()

        db.collection(MerchantListRepository.COLLECTION).firestore.runTransaction {
            val collectionRef = db.collection(MerchantListRepository.COLLECTION)
            val merchantRef = collectionRef.document(merchantId)
            val ratingRef = merchantRef.collection(SUB_COLLECTION_RATINGS).document()

            val merchant: Merchant? = it[merchantRef].toObject(
                Merchant::class.java
            )

            // Compute new number of ratings
            val newNumRatings: Int = merchant!!.numRating.plus(1)

            // Compute new average rating
            val oldRatingTotal: Float = merchant.avgRating * merchant.numRating
            val newAvgRating: Float = (oldRatingTotal + rating.stars) / newNumRatings

            // Set new merchant info
            merchant.numRating = newNumRatings
            merchant.avgRating = newAvgRating

            // Commit to Firestore
            it[merchantRef] = merchant
            it[ratingRef] = rating
        }

        return completion
    }

    fun modifyRating(merchantId: String, rating: Rating) {
        val ratingRef = db.collection(COLLECTION)
            .document(merchantId)
            .collection(SUB_COLLECTION_RATINGS).document(rating.reference)

        rating.editedAt = Timestamp.now()
        ratingRef.set(rating)
    }

    fun deleteRating(merchantId: String, rating: Rating) {
        val ratingRef = db.collection(COLLECTION)
            .document(merchantId)
            .collection(SUB_COLLECTION_RATINGS).document(rating.reference)
        ratingRef.delete()
    }
}