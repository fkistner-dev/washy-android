package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.databinding.LayoutRecyclerListBinding
import com.kilomobi.washy.model.FeedCell
import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewmodel.FeedListViewModel

class TutorialListFragment : FragmentEmptyView(R.layout.layout_recycler_list),
    AdapterListener {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var binding: LayoutRecyclerListBinding
    private val listAdapter by lazy {
        TutorialAdapter(
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            binding = LayoutRecyclerListBinding.bind(view)
            shimmerLayout = view.findViewById(R.id.shimmer_layout)
            initialize()
            viewIsCreated = true
        }
    }

    private fun initialize() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FeedListViewModel::class.java)
        viewModel.getAllFeeds().observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it as List<RecyclerItem>?)
            } else {
                displayEmptyView()
            }
        }

//        viewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.NewInstanceFactory()
//        )[MerchantListViewModel::class.java]
//
//        viewModel.isLoading.observe(requireActivity()) {
//            if (it) {
//                shimmerLayout.visibility = View.VISIBLE
//                shimmerLayout.startShimmer()
//            } else {
//                shimmerLayout.stopShimmer()
//                shimmerLayout.visibility = View.GONE
//            }
//        }

//        viewModel.getAllMerchants().observe(viewLifecycleOwner) {
//            if (it != null && it.isNotEmpty()) {
//                listAdapter.submitList(it as List<RecyclerItem>?)
//            } else {
//                displayEmptyView()
//            }
//        }
    }

    override fun listen(click: AdapterClick?) {
        //TODO("Not yet implemented")
    }
}

open class TutorialAdapter(listener: AdapterListener) : BaseListAdapter(
    FeedCell,
    listener = listener) {
    var selectedItemPosition: Int = -1
}