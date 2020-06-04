package com.kilomobi.washy.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.google.android.material.tabs.TabLayoutMediator
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.FeedPagerAdapter
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.viewmodel.FeedListViewModel

class FeedViewPagerFragment : Fragment(),
    AdapterListener, MainActivityDelegate {

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

        val feedPagerAdapter = FeedPagerAdapter(
            requireContext(),
            feeds
        )

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FeedListViewModel::class.java)
        viewModel.getAllFeeds().observe(viewLifecycleOwner, Observer<List<Feed>> {
            if (it != null && it.isNotEmpty()) {
                feeds.addAll(it)
                feedPagerAdapter.notifyDataSetChanged()
            } else {
                Log.d("TAG", "awaiting for info")
            }
        })

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

    override fun setupNavDrawer(toolbar: Toolbar) {
        TODO("Not yet implemented")
    }

    override fun closeDrawer() {
        TODO("Not yet implemented")
    }

    override fun enableNavDrawer(enable: Boolean) {
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