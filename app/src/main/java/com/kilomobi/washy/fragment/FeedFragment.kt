package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.*
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.model.FeedAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.viewmodel.FeedListViewModel
import kotlinx.android.synthetic.main.layout_recycler_list.*

class FeedFragment(val merchant: Merchant?) : Fragment(), AdapterListener, MainActivityDelegate {

    private lateinit var viewModel: FeedListViewModel
    private val listAdapter by lazy { FeedAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_feed, container, false)
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

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FeedListViewModel::class.java)

        if (merchant == null) {
            viewModel.getAllFeeds().observe(viewLifecycleOwner, Observer<List<Feed>> {
                if (it != null && it.isNotEmpty()) {
                    listAdapter.submitList(it)
                } else {
                    Log.d("TAG", "awaiting for info")
                }
            })
        } else {
            viewModel.getMerchantFeed((merchant?.reference as DocumentReference).id).observe(viewLifecycleOwner, Observer<List<Feed>> {
                if (it != null && it.isNotEmpty()) {
                    listAdapter.submitList(it)
                } else {
                    Log.d("TAG", "awaiting for info")
                }
            })
        }
    }

    override fun listen(click: AdapterClick?) {
        TODO("Not yet implemented")
    }

    override fun setupNavDrawer(toolbar: Toolbar) {
        TODO("Not yet implemented")
    }

    override fun closeDrawer() {
        TODO("Not yet implemented")
    }

    override fun enableNavDrawer(enable: Boolean) {
        TODO("Not yet implemented")
    }
}
//
//object ExampleFeedData {
//
//    fun createList(): List<RecyclerItem> {
//        val list = ArrayList<RecyclerItem>()
//
//        list.add(
//            Feed(
//                name = "Total Wash",
//                message = "Promotion exceptionnelle"
//            )
//        )
//
//        list.add(
//            Feed(
//                name = "Total Wash",
//                message = "Tout pour le déconfinement !"
//            )
//        )
//
//        list.add(
//            Feed(
//                name = "Total Wash",
//                message = "Washement Propwe !"
//            )
//        )
//
//        return list
//    }
//
//}