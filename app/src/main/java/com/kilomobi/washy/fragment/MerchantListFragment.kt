package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.MerchantAdapter
import com.kilomobi.washy.recycler.RecyclerItem
import kotlinx.android.synthetic.main.layout_merchant_tabbed.*
import kotlinx.android.synthetic.main.layout_recycler_list.*

class MerchantListFragment : FragmentEmptyView(R.layout.layout_recycler_list),
    AdapterListener {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var viewModel: MerchantListViewModel
    private val listAdapter by lazy {
        MerchantAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            shimmerLayout = view.findViewById(R.id.shimmer_layout)
            initialize()
            viewIsCreated = true
        }
    }

    private fun initialize() {
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel = ViewModelProvider(this, NewInstanceFactory())[MerchantListViewModel::class.java]

        viewModel.isLoading.observe(requireActivity()) {
            if (it) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
        }

        viewModel.getAllMerchants().observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it as List<RecyclerItem>?)
            } else {
                displayEmptyView()
            }
        }
    }

    override fun listen(click: AdapterClick?) {
        val bundle = bundleOf("merchant" to click)
        findNavController().navigate(R.id.action_merchantListFragment_to_merchantDetailFragment, bundle)
    }
}