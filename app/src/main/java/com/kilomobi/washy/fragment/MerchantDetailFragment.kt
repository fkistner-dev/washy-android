package com.kilomobi.washy.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivity
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.databinding.LayoutMerchantDetailBinding
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Service
import com.kilomobi.washy.viewmodel.MerchantViewModel
import com.kilomobi.washy.viewmodel.UserViewModel
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class MerchantDetailFragment : FragmentEmptyView(R.layout.layout_merchant_detail) {

    private lateinit var mainActivityDelegate: MainActivityDelegate
    private lateinit var merchant: Merchant
    private lateinit var binding: LayoutMerchantDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)

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
            binding = LayoutMerchantDetailBinding.bind(view)
            initialize()
            viewIsCreated = true
        }
    }

    private fun initialize() {
        if (arguments != null && requireArguments()["merchant"] != null && requireArguments()["merchant"] is Merchant) {
            merchant = requireArguments()["merchant"] as Merchant
            fillView()
            setAdapter()
        } else {
            val storeId = findNavController().previousBackStackEntry?.arguments?.getString(MainActivity.STACK_USER_STORE_ID)
            if (!storeId.isNullOrEmpty()) {
                val merchantViewModel = MerchantViewModel()
                merchantViewModel.getMerchant(storeId)?.observe(requireActivity()) { userStore ->
                    merchant = userStore
                    fillView()
                    setAdapter()
                }
            } else {
                // Fallback to user
                val viewModel = UserViewModel()
                FirebaseAuth.getInstance().uid?.let { userId ->
                    viewModel.getUser(userId).observe(requireActivity()) { user ->
                        user?.store?.let {
                            val merchantViewModel = MerchantViewModel()
                            merchantViewModel.getMerchant(it)?.observe(requireActivity()) { userStore ->
                                merchant = userStore
                                fillView()
                                setAdapter()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fillView() {
        currentView?.let { v ->
            if (merchant.imported) {
                v.findViewById<TextView>(R.id.type).visibility = View.GONE
            } else {
                v.findViewById<TextView>(R.id.type).text = if (merchant.siren?.isNotEmpty() == true) getString(R.string.merchant_pro) else getString(R.string.merchant_part)
            }
            v.findViewById<TextView>(R.id.title).text = merchant.name
            v.findViewById<TextView>(R.id.description).text = merchant.description
            v.findViewById<MaterialRatingBar>(R.id.ratingBar).rating = merchant.avgRating

            if (merchant.services.isNotEmpty()) {
                val horizontalScrollView = v.findViewById(R.id.serviceHorizontalScroll) as HorizontalScrollView
                val chipGroup = v.findViewById(R.id.chip_group) as ChipGroup
                horizontalScrollView.visibility = View.VISIBLE

                for (service in merchant.services) {
                    if (service.isBlank()) return
                    val chip = Chip(context)
                    chip.text = Service.retrieveText(service)?.let { context?.getString(it) }
                    chip.chipIcon =
                        Service.retrieveImage(service)
                            ?.let { ContextCompat.getDrawable(requireContext(), it) }
                    chip.setChipIconSizeResource(R.dimen.text_cardview_chip_size)
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.setChipIconTintResource(R.color.colorPrimary)

                    chipGroup.addView(chip)
                }
            }

            v.findViewById<ImageView>(R.id.gmaps).setOnClickListener {
                val merchantPosition = LatLng(merchant.position!!.latitude, merchant.position!!.longitude)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+merchantPosition.latitude+","+merchantPosition.longitude+"?q="+merchantPosition.latitude+","+merchantPosition.longitude+"("+merchant.name+")"))
                startActivity(intent)
            }

            if (merchant.imgUrl?.isNotBlank() == true) {
                v.findViewById<ImageView>(R.id.photo).visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(merchant.imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(v.findViewById(R.id.photo))
            }
        }
    }

    private fun setAdapter() {
        val adapter = MerchantDetailViewPagerFragment(this)
        adapter.addFragment(RatingListFragment(merchant), getString(R.string.feed_title))
        //adapter.addFragment(ProductListFragment(merchant), getString(R.string.offer_title))
        adapter.addFragment(ContactFragment(merchant), getString(R.string.contact_title))

        binding.layoutMerchantTabbedId.viewPager.adapter = adapter
        binding.layoutMerchantTabbedId.viewPager.isSaveEnabled = false

        TabLayoutMediator(binding.layoutMerchantTabbedId.tabLayout, binding.layoutMerchantTabbedId.viewPager) { tab, _ ->
            binding.layoutMerchantTabbedId.viewPager.currentItem = tab.position
        }.attach()

        binding.layoutMerchantTabbedId.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_comment_quote_outline)
        //tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_cart_outline)
        binding.layoutMerchantTabbedId.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_account_box_outline)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> findNavController().popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }
}