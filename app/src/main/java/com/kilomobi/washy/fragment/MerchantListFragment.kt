package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.MerchantAdapter
import com.kilomobi.washy.databinding.LayoutRecyclerListBinding
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.recycler.RecyclerItem

class MerchantListFragment : FragmentEmptyView(R.layout.layout_recycler_list),
    AdapterListener {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var viewModel: MerchantListViewModel
    private lateinit var binding: LayoutRecyclerListBinding
    private val listAdapter by lazy {
        MerchantAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newMerchant = findNavController().currentBackStackEntry?.savedStateHandle?.get<Merchant?>(BecomeWasherFragment.STACK_NEW_MERCHANT)

        if (!viewIsCreated) {
            binding = LayoutRecyclerListBinding.bind(view)
            shimmerLayout = view.findViewById(R.id.shimmer_layout)
            initialize()
            viewIsCreated = true
        } else if (newMerchant != null) {
            // Hack to save a get on Firebase, populate rating with stack
            val currentList = ArrayList<Merchant>()

            // Fill list
            for (merchant in listAdapter.currentList) {
                currentList.add(merchant as Merchant)
            }

            // Add new one at top
            currentList.add(0, newMerchant)
            listAdapter.submitList(currentList.toList())
            listAdapter.notifyItemInserted(0)

            // Clean stack
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Merchant>(BecomeWasherFragment.STACK_NEW_MERCHANT)

            // Handle menu
            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.menu_nav_drawer, menu)
                    menu.findItem(R.id.action_become_washer).isVisible = false
                    menu.findItem(R.id.action_store_washer).isVisible = true
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Item selection managed on MainActivity
                    return false
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun initialize() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel = ViewModelProvider(this, NewInstanceFactory())[MerchantListViewModel::class.java]

        viewModel.isLoading.observe(requireActivity()) {
            if (it) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
        }

        viewModel.getAllMerchants().observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                listAdapter.submitList(it as List<RecyclerItem>?)
            } else {
                displayEmptyView()
            }
        }
    }

    override fun listen(click: AdapterClick?, holder: RecyclerView.ViewHolder?) {
        val bundle = bundleOf("merchant" to click)
        navigate(currentView, R.id.action_merchantListFragment_to_merchantDetailFragment, bundle)
    }
}