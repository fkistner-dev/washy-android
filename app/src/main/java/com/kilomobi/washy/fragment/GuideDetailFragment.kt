package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.databinding.LayoutGuideDetailBinding
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.model.GuideDetailAdapter
import com.kilomobi.washy.model.InnerGuide
import java.lang.Exception

class GuideDetailFragment : FragmentEmptyView(R.layout.layout_guide_detail), AdapterListener {

    companion object {
        const val HEADERS_KEY = "headers"
        const val PREREQUISITES_KEY = "prerequisites"
        const val TEXTS_KEY = "texts"
        const val WARNINGS_KEY = "warnings"
        const val PHOTOS_KEY = "photos"
        const val TOTAL_STEP_KEY = "totalStep"
    }

    private lateinit var mainActivityDelegate: MainActivityDelegate
    private lateinit var guide: Guide
    private lateinit var binding: LayoutGuideDetailBinding
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
            binding = LayoutGuideDetailBinding.bind(view)
            initialize()
            viewIsCreated = true
        }
    }

    private fun initialize() {
        if (arguments != null && requireArguments()["guide"] != null && requireArguments()["guide"] is Guide) {
            guide = requireArguments()["guide"] as Guide
            fillView()
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

    private fun convertToInnerGuide(guide: Guide) : List<InnerGuide> {
        val headers = guide.innerGuide[HEADERS_KEY] as ArrayList<String>
        val prerequisites = guide.innerGuide[PREREQUISITES_KEY] as ArrayList<String>
        val texts = guide.innerGuide[TEXTS_KEY] as ArrayList<String>
        val warnings = guide.innerGuide[WARNINGS_KEY] as ArrayList<String>
        val photos = guide.innerGuide[PHOTOS_KEY] as ArrayList<String>
        val totalStep = (guide.innerGuide[TOTAL_STEP_KEY] as Long).toInt()

        val innerGuideList = ArrayList<InnerGuide>()
        for (i in 0 until totalStep) {
            val innerGuide = InnerGuide()
            try {
                innerGuide.header = headers[i]
                innerGuide.prerequisite = prerequisites[i]
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