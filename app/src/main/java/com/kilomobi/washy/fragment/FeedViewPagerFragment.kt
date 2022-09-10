package com.kilomobi.washy.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.FeedPagerAdapter
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.viewmodel.FeedListViewModel

class FeedViewPagerFragment : FragmentEmptyView(R.layout.layout_feed_viewpager),
    AdapterListener {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var viewPager: ViewPager2
    private val feeds = ArrayList<Feed>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_feed_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.viewPager)

        shimmerLayout = view.findViewById(R.id.shimmer_layout)

        val feedPagerAdapter = FeedPagerAdapter(
            requireContext(),
            feeds
        )

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FeedListViewModel::class.java]
        viewModel.isLoading.observe(requireActivity()) {
            if (it) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
        }

        viewModel.getAllFeeds().observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                feeds.addAll(it)
                feedPagerAdapter.notifyDataSetChanged()
            } else {
                displayEmptyView()
            }
        }

        viewPager.orientation = ORIENTATION_HORIZONTAL
        viewPager.adapter = feedPagerAdapter
        viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
        }
        viewPager.setPageTransformer(pageTransformer)

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration =
            HorizontalMarginItemDecoration(
                requireContext(),
                R.dimen.viewpager_current_item_horizontal_margin
            )

        viewPager.addItemDecoration(itemDecoration)

        TabLayoutMediator(view.findViewById(R.id.tabLayout), viewPager) { tab, position ->}.attach()
    }


    override fun listen(click: AdapterClick?) {
        TODO("Not yet implemented")
    }

    /**
     * Adds margin to the left and right sides of the RecyclerView item.
     * Adapted from https://stackoverflow.com/a/27664023/4034572
     * @param horizontalMarginInDp the margin resource, in dp.
     */
    class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
        RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
            context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val itemCount = state.itemCount

            if (position != 0) outRect.left = horizontalMarginInPx
            if (position != itemCount -1) outRect.right = horizontalMarginInPx
        }
    }
}