package com.kilomobi.washy.merchant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kilomobi.washy.R
import com.kilomobi.washy.Service
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.db.merchant.Merchant
import kotlinx.android.synthetic.main.fragment_list_merchant.*

class MerchantListFragment : Fragment(),
    AdapterListener {

    private val listAdapter by lazy {
        MerchantAdapter(
            this
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_merchant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        listAdapter.submitList(ExampleMerchantData.createList())
    }

    override fun listen(click: AdapterClick?) {
        listAdapter.selectedItemPosition = (click as Merchant).id?.toInt()!!
        listAdapter.notifyDataSetChanged()
    }

    fun updateListPosition(id: Int) {
        listAdapter.selectedItemPosition = id
        listAdapter.notifyDataSetChanged()
        recycler.smoothScrollToPosition(id)
    }
}

object ExampleMerchantData {

    fun createList(): List<RecyclerItem> {
        val list = ArrayList<RecyclerItem>()

        list.add(
            Merchant(
                id = "0",
                name = "Total Wash",
                address = "Avenue Pierre Mendès-France, 67300 Schiltigheim",
                latitude = 48.6f,
                longitude = 49.7f,
                presentation = "Vous ne connaissez pas encore TOTAL Wash ? C'est l'offre de lavage auto et moto de Total présente dans près de 1000 stations-service du réseau Total.",
                priceStart = 4.50,
                rating = 2.6f,
                services = Service.servicesToString(Service.COFFEE.first, Service.BIKE.first, Service.WIFI.first, Service.TRANSPORT.first)
            )
        )

        list.add(
            Merchant(
                id = "1",
                name = "JL Lavage",
                address = "34 Rue Principale, 67350 La Walck",
                latitude = 48.2f,
                longitude = 49.7f,
                presentation = "JL Lavage est un établissement situé à La Wack, spécialisé dans le lavage de véhicules de luxe depuis 5 ans. Sur place, une équipe de professionnels mettant leur expérience et leur savoir-faire au service de chaque conducteur, pour rendre à chaque véhicule la splendeur de ses premiers kilomètres.",
                priceStart = 45.0,
                rating = 4.6f,
                services = Service.servicesToString(Service.CERAMIC.first, Service.ECO_FRIENDLY.first, Service.HAND_WASH.first)
            )
        )

        list.add(
            Merchant(
                id = "2",
                name = "LK Renov Auto",
                address = "4 Rue du Village, 67170 Donnenheim",
                latitude = 48.4f,
                longitude = 49.7f,
                presentation = "Profiter d’une rénovation d’optiques des phares dans un centre qualifié et spécialisé dans l’entretien et le nettoyage automobile",
                priceStart = 12.0,
                rating = 1f,
                services = Service.servicesToString(Service.COFFEE.first, Service.WIFI.first)
            )
        )

        list.add(
            Merchant(
                id = "3",
                name = "MacWash",
                address = "10 Rue des Églantines, 68040 Ingersheim",
                latitude = 48.5f,
                longitude = 49.7f,
                presentation = "Texte de presentation rapide",
                priceStart = 99.0,
                rating = 5f,
                services = Service.servicesToString(Service.HAND_WASH.first)
            )
        )

        return list
    }

}