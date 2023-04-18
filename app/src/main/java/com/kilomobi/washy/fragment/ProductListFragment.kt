package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.ProductAdapter
import com.kilomobi.washy.databinding.LayoutRecyclerListBinding
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewmodel.MerchantViewModel

class ProductListFragment(val merchant: Merchant) : FragmentEmptyView(R.layout.layout_recycler_list), AdapterListener {

    private lateinit var viewModel: MerchantViewModel
    private var viewContainer: ViewGroup? = null
    private lateinit var binding: LayoutRecyclerListBinding
    private val listAdapter by lazy {
        ProductAdapter(
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewContainer = container
        return inflater.inflate(R.layout.layout_recycler_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ShimmerFrameLayout>(R.id.shimmer_layout).visibility = View.GONE
        initialize()
    }

    private fun initialize() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MerchantViewModel::class.java]
        viewModel.getProducts(merchant.reference!!).observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it as List<RecyclerItem>?)
            } else {
                displayEmptyView()
            }
        }
    }

    override fun listen(click: AdapterClick?, holder: RecyclerView.ViewHolder?) { }
}