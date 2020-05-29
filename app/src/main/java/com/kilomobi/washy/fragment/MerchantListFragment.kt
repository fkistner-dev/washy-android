package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.MerchantAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.recycler.RecyclerItem
import kotlinx.android.synthetic.main.fragment_list_merchant.*

class MerchantListFragment : Fragment(),
    AdapterListener {

    private var viewModel: MerchantListViewModel? = null
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

        // View model
        viewModel = ViewModelProvider(this, NewInstanceFactory()).get(MerchantListViewModel::class.java)
        viewModel!!.getAllMerchants().observe(viewLifecycleOwner, Observer<List<Merchant>> {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it)
            } else {
                Log.d("TAG", "awaiting for info")
            }
        })
    }

    override fun listen(click: AdapterClick?) {
//        listAdapter.selectedItemPosition = (click as Merchant)
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

//        list.add(
//            Merchant(
//                name = "Total Wash",
//                presentation = "Vous ne connaissez pas encore TOTAL Wash ? C'est l'offre de lavage auto et moto de Total présente dans près de 1000 stations-service du réseau Total.",
//                priceStart = 4.50,
//                rating = 2.6f,
//                services = Service.servicesToString(Service.COFFEE.first, Service.BIKE.first, Service.WIFI.first, Service.TRANSPORT.first),
//                adminId = 0
//            )
//        )
//
//        list.add(
//            Merchant(
//                name = "JL Lavage",
//                presentation = "JL Lavage est un établissement situé à La Wack, spécialisé dans le lavage de véhicules de luxe depuis 5 ans. Sur place, une équipe de professionnels mettant leur expérience et leur savoir-faire au service de chaque conducteur, pour rendre à chaque véhicule la splendeur de ses premiers kilomètres.",
//                priceStart = 45.0,
//                rating = 4.6f,
//                services = Service.servicesToString(Service.CERAMIC.first, Service.ECO_FRIENDLY.first, Service.HAND_WASH.first),
//                adminId = 0
//            )
//        )
//
//        list.add(
//            Merchant(
//                name = "LK Renov Auto",
//                presentation = "Profiter d’une rénovation d’optiques des phares dans un centre qualifié et spécialisé dans l’entretien et le nettoyage automobile",
//                priceStart = 12.0,
//                rating = 1f,
//                services = Service.servicesToString(Service.COFFEE.first, Service.WIFI.first),
//                adminId = 0
//            )
//        )
//
//        list.add(
//            Merchant(
//                name = "MacWash",
//                presentation = "Texte de presentation rapide",
//                priceStart = 99.0,
//                rating = 5f,
//                services = Service.servicesToString(Service.HAND_WASH.first),
//                adminId = 0
//            )
//        )

        return list
    }

}