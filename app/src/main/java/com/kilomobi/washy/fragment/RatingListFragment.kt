package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.RatingAdapter
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.viewmodel.MerchantViewModel
import kotlinx.android.synthetic.main.layout_recycler_list.*

class RatingListFragment : Fragment(), AdapterListener {

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
    ): View? {

        return inflater.inflate(R.layout.layout_recycler_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MerchantViewModel::class.java)
        viewModel.getRatings(((arguments?.get("merchant") as Merchant).reference as DocumentReference).id).observe(viewLifecycleOwner, Observer<List<Rating>> {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it)
            } else {
                Log.d("TAG", "awaiting for info")
            }
        })
    }

    override fun listen(click: AdapterClick?) {
        TODO("Not yet implemented")
    }
}