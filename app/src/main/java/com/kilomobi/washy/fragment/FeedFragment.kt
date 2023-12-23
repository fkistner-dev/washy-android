/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.*
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.databinding.LayoutFeedBinding
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.model.FeedAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewmodel.FeedListViewModel

class FeedFragment(val merchant: Merchant? = null) : FragmentEmptyView(R.layout.layout_feed), AdapterListener {

    private lateinit var viewModel: FeedListViewModel
    private val listAdapter by lazy { FeedAdapter(this) }
    private lateinit var binding: LayoutFeedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LayoutFeedBinding.bind(view)
        initialize()
    }

    private fun initialize() {
        binding.recycler.apply {
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
            viewModel.getMerchantFeed(merchant.reference!!).observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    listAdapter.submitList(it as List<RecyclerItem>?)
                } else {
                    displayEmptyView()
                }
            }
        }
    }

    override fun listen(click: AdapterClick?, holder: RecyclerView.ViewHolder?) { }
}