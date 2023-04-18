package com.kilomobi.washy.fragment

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Bundle
import android.text.TextUtils
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.RatingAdapter
import com.kilomobi.washy.databinding.LayoutRecyclerListBinding
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewmodel.MerchantViewModel

class RatingListFragment(val merchant: Merchant? = null) : FragmentEmptyView(R.layout.layout_recycler_list), AdapterListener {

    companion object {
        const val STACK_CURRENT_RATING: String = "rating"
        const val STACK_PREVIOUS_RATING: String = "previousRating"
        const val STACK_IS_DELETE_RATING: String = "shouldDelete"
    }

    private lateinit var viewModel: MerchantViewModel
    private var userRating: Rating? = null
    private lateinit var binding: LayoutRecyclerListBinding
    private lateinit var fab: FloatingActionButton
    private val listAdapter by lazy {
        RatingAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewIsCreated) {
            binding = LayoutRecyclerListBinding.bind(view)

            // Add Fab
            if (isConnected()) {
                val user = FirebaseAuth.getInstance().currentUser

                // Washer cannot evaluate itself
                if (user?.uid != merchant?.adminId) {
                    val context: Context = ContextThemeWrapper(context, R.style.FabAddButton)
                    fab = FloatingActionButton(context)
                    fab.layoutParams = ViewGroup.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    val layout = (view as ConstraintLayout)
                    val set = ConstraintSet()
                    fab.id = View.generateViewId() // cannot set id after add
                    fab.setOnClickListener {
                        if (isConnected(true)) {
                            val bundle = bundleOf("merchant" to merchant)
                            userRating?.let { bundle.putSerializable("rating", it) }
                            if (userRating != null && TextUtils.isEmpty(userRating?.reference)) {
                                // We have no reference yet to this rating
                                fab.isEnabled = false
                                Snackbar.make(requireView(), getString(R.string.snack_syncing_message), Snackbar.LENGTH_LONG).show()
                            } else {
                                findNavController().navigate(
                                    R.id.action_merchantDetailFragment_to_addRatingFragment,
                                    bundle)
                            }
                        }
                    }
                    fab.setImageIcon(Icon.createWithResource(context, android.R.drawable.ic_input_add))
                    fab.elevation = 6f
                    layout.addView(fab, 0)
                    set.clone(layout)
                    set.connect(fab.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, resources.getDimension(R.dimen.text_default).toInt())
                    set.connect(fab.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, resources.getDimension(R.dimen.text_default).toInt())
                    set.applyTo(layout)
                }
            }

            view.findViewById<ShimmerFrameLayout>(R.id.shimmer_layout).visibility = View.GONE
            initialize()

            viewIsCreated = true
        } else {
            val currentRating = findNavController().currentBackStackEntry?.savedStateHandle?.get<Rating>(STACK_CURRENT_RATING)
            val previousRating = findNavController().currentBackStackEntry?.savedStateHandle?.get<Rating?>(STACK_PREVIOUS_RATING)

            // Hack to save a get on Firebase, populate rating with stack
            val currentList = ArrayList<Rating>()

            // Fill list
            for (rating in listAdapter.currentList) {
                currentList.add(rating as Rating)
            }

            if (previousRating == null && currentList.isNotEmpty()) {
                // Add new rating
                //currentList.removeAt(0)
            } else if (previousRating != null) {
                // Modify|Delete existing
                currentList.remove(previousRating)
            }

            // Add new or modified one at top
            currentRating?.let {
                currentList.add(0, it)
            }

            // Show accordingly fab icon
            fab.setImageIcon(Icon.createWithResource(context, if (currentRating != null) android.R.drawable.ic_menu_edit else android.R.drawable.ic_input_add))

            listAdapter.submitList(currentList.toList())
            listAdapter.notifyDataSetChanged()

            userRating = currentRating

            if (listAdapter.currentList.isNotEmpty())
                hideEmptyView()

            // Remove object from previousStack
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Rating>(STACK_CURRENT_RATING)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Rating>(STACK_PREVIOUS_RATING)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(STACK_IS_DELETE_RATING)
        }
    }

    private fun initialize() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        listAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.recycler.smoothScrollToPosition(0)
            }
        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MerchantViewModel::class.java]
        viewModel.getRatings(merchant?.reference!!).observe(viewLifecycleOwner
        ) { ratingList ->
            if (ratingList != null && ratingList.isNotEmpty()) {
                listAdapter.submitList(ratingList as List<RecyclerItem>?)
                if (isConnected()) {
                    userRating = ratingList.find { it.userId == FirebaseAuth.getInstance().uid }
                    userRating?.let { fab.setImageIcon(Icon.createWithResource(context, android.R.drawable.ic_menu_edit)) }
                }
            } else {
                displayEmptyView()
            }
        }
    }

    override fun listen(click: AdapterClick?, holder: RecyclerView.ViewHolder?) { }
}