package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.ProductAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Product
import com.kilomobi.washy.viewmodel.MerchantViewModel
import kotlinx.android.synthetic.main.layout_recycler_list.*

class ProductListFragment(val merchant: Merchant) : FragmentEmptyView(), AdapterListener {

    private lateinit var viewModel: MerchantViewModel
    private var viewContainer: ViewGroup? = null
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
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MerchantViewModel::class.java)
        viewModel.getProducts((merchant.reference as DocumentReference).id).observe(viewLifecycleOwner, Observer<List<Product>> {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it)
            } else {
                displayEmptyView()
            }
        })
    }

    override fun listen(click: AdapterClick?) {
        TODO("Not yet implemented")
    }
}