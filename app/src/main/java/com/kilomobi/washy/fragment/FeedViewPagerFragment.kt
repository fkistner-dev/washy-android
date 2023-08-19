package com.kilomobi.washy.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.FeedPagerAdapter
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.viewmodel.FeedListViewModel

class FeedViewPagerFragment : FragmentEmptyView(R.layout.layout_feed_viewpager),
    FeedPagerAdapter.FeedPagerListener {

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

        view.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        val feedPagerAdapter = FeedPagerAdapter(
            requireContext(),
            feeds,
            this
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

        TabLayoutMediator(view.findViewById(R.id.tabLayout), viewPager) { tab, position -> }.attach()

        setExitToFullScreenTransition()
        setReturnFromFullScreenTransition()
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

    override fun listen(holder: FeedPagerAdapter.FeedViewHolder, feed: Feed) {
        if (feed.guideLink.isNotEmpty())
            navigateToGuideDetail(feed)
        else
            navigateToFeedDetail(holder, feed)
    }

    private fun navigateToGuideDetail(feed: Feed) {
        val bundle = bundleOf("feed" to feed)
        navigate(currentView, R.id.action_homeFragment_to_guideDetailFragment, bundle)
    }
    private fun navigateToFeedDetail(holder: FeedPagerAdapter.FeedViewHolder, feed: Feed) {
        val bundle = bundleOf("feed" to feed)

        val darkViewPair =  holder.darken to "big_darken_" + feed.hashCode().toString().ifEmpty { "nullDarken" }
        val photoPair =  holder.image to "big_" + feed.photos[0].ifEmpty { "nullPhoto" }
        val circlePair = holder.circleImage to "big_" + feed.hashCode().toString().ifEmpty { "nullCircle" }
        val headerPair = holder.header to "big_" + feed.cardviewHeader.ifEmpty { "nullHeader" }
        val textPair = holder.text to "big_" + feed.cardviewText.ifEmpty { "nullText" }

        ViewCompat.setTransitionName(holder.darken, "small_darken_" + feed.hashCode().toString().ifEmpty { "nullDarken" })
        ViewCompat.setTransitionName(holder.image, "small_" + feed.photos[0].ifEmpty { "nullPhoto" })
        ViewCompat.setTransitionName(holder.circleImage, "small_" + feed.hashCode().toString().ifEmpty { "nullCircle" })
        ViewCompat.setTransitionName(holder.header, "small_" + feed.cardviewHeader.ifEmpty { "nullHeader" })
        ViewCompat.setTransitionName(holder.text, "small_" + feed.cardviewText.ifEmpty { "nullText" })

        val extras = FragmentNavigatorExtras(darkViewPair, photoPair, circlePair, headerPair, textPair)
        navigate(currentView, R.id.action_homeFragment_to_feedDetailFragment, bundle, navExtra = extras)
    }

    private fun setExitToFullScreenTransition() {
        exitTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.feed_list_exit_transition)
    }

    private fun setReturnFromFullScreenTransition() {
        reenterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.feed_list_return_transition)
    }
}