package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.databinding.LayoutRecyclerGuideListBinding
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.model.GuideListAdapter
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewholder.GuideListViewHolder
import com.kilomobi.washy.viewmodel.GuideListViewModel

class GuideListFragment : FragmentEmptyView(R.layout.layout_recycler_guide_list),
    AdapterListener {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var binding: LayoutRecyclerGuideListBinding
    private val listAdapter by lazy {
        GuideListAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            binding = LayoutRecyclerGuideListBinding.bind(view)
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

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[GuideListViewModel::class.java]

        viewModel.isLoading.observe(requireActivity()) {
            if (it) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
        }

        viewModel.getAllGuides().observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it as List<RecyclerItem>?)
            } else {
                displayEmptyView()
            }
        }
    }

    override fun listen(click: AdapterClick?, holder: RecyclerView.ViewHolder?) {
        val bundle = bundleOf("guide" to click)

        holder?.let { item ->
            if (item is GuideListViewHolder && click is Guide) {
                val imageView = item.itemView.findViewById<ImageView>(R.id.image)
                ViewCompat.setTransitionName(imageView, "small_" + click.photo)
                val extras = FragmentNavigatorExtras(
                    imageView to "big_" + click.photo,
                )

                navigate(currentView, R.id.action_guideListFragment_to_guideDetailFragment, bundle)
            }
        }
    }
}