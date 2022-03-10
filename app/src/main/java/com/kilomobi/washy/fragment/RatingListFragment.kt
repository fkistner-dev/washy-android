package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.RatingAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.viewmodel.FeedListViewModel
import com.kilomobi.washy.viewmodel.MerchantViewModel
import kotlinx.android.synthetic.main.layout_recycler_list.*

class RatingListFragment(val merchant: Merchant? = null) : FragmentEmptyView(), AdapterListener {

    private var viewContainer: ViewGroup? = null
    private lateinit var viewModel: MerchantViewModel
    private val listAdapter by lazy {
        RatingAdapter(
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

        viewModel.getRatings((merchant?.reference as DocumentReference).id).observe(viewLifecycleOwner, Observer<List<Rating>> {
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