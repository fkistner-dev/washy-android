package com.kilomobi.washy.fragment

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.RatingAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Rating
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
    ): View {
        Log.d(RatingListFragment::class.java.canonicalName, "onCreateView")
        viewContainer = container
        val view = inflater.inflate(R.layout.layout_recycler_list, container, false)

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
            val bundle = bundleOf("merchant" to merchant)
            findNavController().navigate(R.id.action_merchantDetailFragment_to_addRatingFragment, bundle)
        }
        fab.setImageIcon(Icon.createWithResource(context, android.R.drawable.ic_input_add))
        fab.elevation = 6f
        layout.addView(fab, 0)
        set.clone(layout)
        set.connect(fab.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, resources.getDimension(R.dimen.text_default).toInt())
        set.connect(fab.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, resources.getDimension(R.dimen.text_default).toInt())
        set.applyTo(layout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(RatingListFragment::class.java.canonicalName, "onViewCreated")
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