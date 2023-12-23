/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.databinding.LayoutPhotolabBinding
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.model.FeedAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewmodel.FeedListViewModel

class PhotoLabFragment(val merchant: Merchant? = null) : FragmentEmptyView(R.layout.layout_photolab), AdapterListener {

    private lateinit var viewModel: FeedListViewModel
    private lateinit var binding: LayoutPhotolabBinding
    private val listAdapter by lazy { FeedAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewIsCreated) {
            initialize()
            setHasOptionsMenu(true)
            viewIsCreated = true
        }
    }

    private fun initialize() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FeedListViewModel::class.java)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_photo_lab, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_photo -> {
                navigate(currentView, R.id.action_photolabFragment_to_addPhotoFragment, null)
                return true
            }
            android.R.id.home -> findNavController().popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun listen(click: AdapterClick?, holder: RecyclerView.ViewHolder?) { }
}