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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.databinding.LayoutRecyclerGuideDetailBinding
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.model.GuideDetailAdapter
import com.kilomobi.washy.model.InnerGuide
import com.kilomobi.washy.viewmodel.GuideListViewModel

class GuideDetailFragment : FragmentEmptyView(R.layout.layout_recycler_guide_detail), AdapterListener {

    companion object {
        const val TEXTS_KEY = "texts"
        const val WARNINGS_KEY = "warnings"
        const val PHOTOS_KEY = "photos"
        const val TOTAL_STEP_KEY = "totalStep"
    }

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var mainActivityDelegate: MainActivityDelegate
    private lateinit var guide: Guide
    private lateinit var binding: LayoutRecyclerGuideDetailBinding
    private val listAdapter by lazy { GuideDetailAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        return currentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            binding = LayoutRecyclerGuideDetailBinding.bind(view)
            shimmerLayout = view.findViewById(R.id.shimmer_layout)
            initialize()
            viewIsCreated = true
        }
    }

    private fun initialize() {
        if (arguments != null && requireArguments()["guide"] != null && requireArguments()["guide"] is Guide) {
            guide = requireArguments()["guide"] as Guide
            fillView()
        } else if (arguments != null && requireArguments()["feed"] != null && requireArguments()["feed"] is Feed) {
            val feed = requireArguments()["feed"] as Feed
            retrieveGuide(feed.guideLink.ifEmpty { feed.linkToAction })
        }
    }

    private fun fillView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        // Reconstruct the list of InnerGuide
        val innerGuide = convertToInnerGuide(guide)
        listAdapter.submitList(innerGuide)
    }

    private fun retrieveGuide (guideLink: String) {
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

        viewModel.getGuide(guideLink).observe(viewLifecycleOwner) {
            if (it != null && it.title.isNotEmpty()) {
                guide = it
                fillView()
            } else {
                Snackbar.make(requireView(), getString(R.string.error_guide_not_found), Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun convertToInnerGuide(guide: Guide) : List<InnerGuide> {
        val texts = guide.innerGuide[TEXTS_KEY] as ArrayList<String>
        val warnings = guide.innerGuide[WARNINGS_KEY] as ArrayList<String>
        val photos = guide.innerGuide[PHOTOS_KEY] as ArrayList<String>
        val totalStep = (guide.innerGuide[TOTAL_STEP_KEY] as Long).toInt()

        val innerGuideList = ArrayList<InnerGuide>()
        for (i in 0 until totalStep) {
            val innerGuide = InnerGuide()
            try {
                innerGuide.text = texts[i]
                innerGuide.warning = warnings[i]
                innerGuide.photo = photos[i]
                innerGuideList.add(innerGuide)
            } catch (_: Exception) { }
        }

        return innerGuideList
    }

    override fun listen(click: AdapterClick?, holder: RecyclerView.ViewHolder?) { }
}