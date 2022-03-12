package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.*
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.model.FeedAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.viewmodel.FeedListViewModel
import kotlinx.android.synthetic.main.layout_recycler_list.*

class FeedFragment(val merchant: Merchant? = null) : FragmentEmptyView(), AdapterListener {

    private lateinit var viewModel: FeedListViewModel
    private var viewContainer: ViewGroup? = null
    private val listAdapter by lazy { FeedAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewContainer = container
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
                    displayEmptyView()
                }
            })
        } else {
            viewModel.getMerchantFeed((merchant.reference as DocumentReference).id).observe(viewLifecycleOwner, Observer<List<Feed>> {
                if (it != null && it.isNotEmpty()) {
                    listAdapter.submitList(it)
                } else {
                    displayEmptyView()
                }
            })
        }
    }

    override fun listen(click: AdapterClick?) {
        TODO("Not yet implemented")
    }
}