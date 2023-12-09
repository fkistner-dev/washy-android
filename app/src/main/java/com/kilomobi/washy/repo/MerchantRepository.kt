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

    private val feedArray = arrayOf(
        "Service rapide et efficace. Mon véhicule brille comme neuf à chaque visite.",
        "L'équipe est sympathique et le lavage est de qualité. Je suis un client fidèle.",
        "Des résultats impressionnants à chaque fois. Mon choix numéro un pour le lavage auto.",
        "Des installations propres et un service client exceptionnel. Recommande fortement !",
        "Aucune rayure, un nettoyage en profondeur. Mon véhicule n'a jamais été aussi propre.",
        "Le lavage express est idéal lorsque vous êtes pressé. Excellent rapport qualité-prix.",
        "Le lavage intérieur était incroyable. Ils ont même pris soin des détails.",
        "Mon expérience a été fantastique. Le personnel est professionnel et courtois.",
        "J'utilise ce service depuis des années. Ils maintiennent toujours une qualité constante.",
        "L'équipe est attentive aux détails. Mon véhicule est entre de bonnes mains ici.",
        "Le service était correct. Rien d'extraordinaire, mais rien de catastrophique non plus.",
        "Pas mal, mais j'ai vu des endroits qui ont été manqués. Peut-être besoin d'un peu plus d'attention aux détails.",
        "Assez satisfait. Le lavage extérieur était bien, mais l'intérieur aurait pu être meilleur.",
        "Service décent pour le prix. Les résultats étaient acceptables.",
        "Légèrement au-dessus de la moyenne. Rien d'exceptionnel, mais ça fait le travail.",
        "Dans l'ensemble, un bon service. Le lavage extérieur était bien fait.",
        "Bon rapport qualité-prix. Le personnel était sympathique, mais le service pourrait être amélioré.",
        "J'ai été agréablement surpris. La qualité était meilleure que ce à quoi je m'attendais.",
        "Très satisfait du lavage extérieur. L'intérieur était correct.",
        "Service solide. Les résultats étaient au-dessus de la moyenne.",
        "Assez impressionné. Ils ont fait un bon travail sur les taches difficiles.",
        "Je reviendrai. Le service global était bon.",
        "J'ai eu un excellent service à chaque visite. Les résultats sont toujours constants.",
        "Le personnel est attentif aux détails. Je suis très satisfait.",
        "Un excellent service client et des résultats de lavage exceptionnels.",
        "La qualité du lavage est remarquable. Ils ont pris soin des petites imperfections.",
        "Chaque fois que je vais là-bas, je suis étonné de la qualité du travail. Les employés sont vraiment professionnels.",
        "Des résultats exceptionnels. Mon véhicule est comme neuf après chaque visite.",
        "Je recommande vivement ce service. Ils vont au-delà des attentes.",
        "Une expérience fantastique à chaque fois. Le lavage intérieur est impeccable.",
        "Le lavage extérieur et intérieur était impeccable. Service exceptionnel.",
        "Ils ont pris soin des détails que d'autres services ont négligés. Très impressionné.",
        "La propreté de mon véhicule après le lavage est incomparable. Le meilleur service.",
        "L'équipe fait un travail exceptionnel. Mon véhicule n'a jamais été aussi propre.",
        "La qualité est toujours au rendez-vous. Ils maintiennent des normes élevées.",
        "Des résultats de lavage exceptionnels. Je suis un client fidèle.",
        "Le lavage intérieur était incroyable. Ils ont même nettoyé les taches difficiles.",
        "Service client impeccable et résultats de lavage de première classe.",
        "La qualité est constante. Je recommande vivement ce service de lavage auto.",
        "Ils vont au-delà de mes attentes à chaque visite. Service de première classe."
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
        feedArray.shuffle()
        val selectedFeeds = feedArray.asList().subList(0, minOf(feedNumber, feedArray.size))

        for (avis in selectedFeeds) {
            val rating = Rating(text = avis, userName = firstNames[Random.nextInt(0, 49)], verified = true, stars = Random.nextInt(1,5).toFloat())
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