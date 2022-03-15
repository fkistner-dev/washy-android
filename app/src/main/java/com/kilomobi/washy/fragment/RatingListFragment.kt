package com.kilomobi.washy.fragment

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.RatingAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewmodel.MerchantViewModel
import kotlinx.android.synthetic.main.layout_recycler_list.*

class RatingListFragment(val merchant: Merchant? = null) : FragmentEmptyView(R.layout.layout_recycler_list), AdapterListener {

    private lateinit var viewModel: MerchantViewModel
    private var userRating: Rating? = null
    private val listAdapter by lazy {
        RatingAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewIsCreated) {

            // Add Fab
            val context: Context = ContextThemeWrapper(context, R.style.FabAddButton)
            val fab = FloatingActionButton(context)
            fab.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val layout = (view as ConstraintLayout)
            val set = ConstraintSet()
            fab.id = View.generateViewId() // cannot set id after add
            fab.setOnClickListener {
                if (isConnected()) {
                    val bundle = bundleOf("merchant" to merchant)
                    userRating?.let { bundle.putSerializable("rating", it) }
                    findNavController().navigate(
                        R.id.action_merchantDetailFragment_to_addRatingFragment,
                        bundle)
                }
            }
            fab.setImageIcon(Icon.createWithResource(context, android.R.drawable.ic_input_add))
            fab.elevation = 6f
            layout.addView(fab, 0)
            set.clone(layout)
            set.connect(fab.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, resources.getDimension(R.dimen.text_default).toInt())
            set.connect(fab.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, resources.getDimension(R.dimen.text_default).toInt())
            set.applyTo(layout)

            view.findViewById<ShimmerFrameLayout>(R.id.shimmer_layout).visibility = View.GONE
            initialize()

            viewIsCreated = true
        } else {
            val previousRating = findNavController().currentBackStackEntry?.savedStateHandle?.get<Rating?>("previousRating")
            findNavController().currentBackStackEntry?.savedStateHandle?.get<Rating>("rating")?.let {
                // Hack to save a get on Firebase, populate rating with stack
                val currentList = ArrayList<Rating>()

                // Fill list
                for (rating in listAdapter.currentList) {
                    currentList.add(rating as Rating)
                }

                if (previousRating == null && currentList.isNotEmpty()) {
                    // Add new rating
                    currentList.removeAt(0)
                } else {
                    // Modify existing
                    currentList.remove(previousRating)
                }

                // Add new or modified one at top
                currentList.add(0, it)

                listAdapter.submitList(currentList.toList())
                listAdapter.notifyItemInserted(0)
                userRating = it
                hideEmptyView()
            }

            // Remove object from previousStack
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Rating>("rating")
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Rating>("previousRating")
        }
    }

    private fun initialize() {
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        listAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                recycler.smoothScrollToPosition(0)
            }
        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MerchantViewModel::class.java]
        viewModel.getRatings(merchant?.reference!!).observe(viewLifecycleOwner
        ) { ratingList ->
            if (ratingList != null && ratingList.isNotEmpty()) {
                listAdapter.submitList(ratingList as List<RecyclerItem>?)
                if (isConnected())
                    userRating = ratingList.find { it.userId == FirebaseAuth.getInstance().uid }
            } else {
                displayEmptyView()
            }
        }
    }

    override fun listen(click: AdapterClick?) { }
}