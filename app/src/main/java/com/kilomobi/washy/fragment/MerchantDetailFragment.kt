package com.kilomobi.washy.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Service
import com.kilomobi.washy.util.initToolbar
import kotlinx.android.synthetic.main.layout_merchant_tabbed.*
//import kotlinx.android.synthetic.main.layout_top_bar.*
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class MerchantDetailFragment : Fragment() {

    private lateinit var mainActivityDelegate: MainActivityDelegate
    private lateinit var merchant: Merchant
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_merchant_detail, container, false)

        setHasOptionsMenu(true)

        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        if (arguments != null && requireArguments()["merchant"] != null && requireArguments()["merchant"] is Merchant) {
            merchant = requireArguments()["merchant"] as Merchant
        }

        if (merchant.imported) {
            view.findViewById<TextView>(R.id.type).visibility = View.GONE
        }
        view.findViewById<TextView>(R.id.type).text = if (merchant.siren.isNotEmpty()) getString(R.string.merchant_pro) else getString(R.string.merchant_part)
        view.findViewById<TextView>(R.id.title).text = merchant.name
        view.findViewById<TextView>(R.id.description).text = merchant.description
        view.findViewById<MaterialRatingBar>(R.id.ratingBar).rating = if (merchant.avgRating < 0.1f) merchant.googleAvgRating else merchant.avgRating

        val serviceScrollView = view.findViewById<HorizontalScrollView>(R.id.serviceHorizontalScroll) as ViewGroup
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        for (service in merchant.services) {
            if (service.isBlank()) return null
            val chip = Chip(requireContext())
            chip.text = service
            chip.minHeight = 16
            chip.chipIcon = ContextCompat.getDrawable(requireContext(), Service.retrieveImage(service))
            chip.setChipBackgroundColorResource(R.color.white)
            chip.setChipIconTintResource(R.color.colorPrimary)
            linearLayout.addView(chip)

            val emptyView = View(requireContext())
            emptyView.layoutParams = ViewGroup.LayoutParams(12, 0)
            linearLayout.addView(emptyView)
        }

        serviceScrollView.addView(linearLayout)

        view.findViewById<ImageView>(R.id.gmaps).setOnClickListener {
            val merchantPosition = LatLng(merchant.position!!.latitude, merchant.position!!.longitude)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+merchantPosition.latitude+","+merchantPosition.longitude+"?q="+merchantPosition.latitude+","+merchantPosition.longitude+"("+merchant.name+")"))
                startActivity(intent)
        }

        if (!merchant.imgUrl.isBlank()) {
            view.findViewById<ImageView>(R.id.photo).visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(merchant.imgUrl)
                .into(view.findViewById<ImageView>(R.id.photo))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initToolbar(toolbar, true)

        val adapter = MerchantDetailViewPagerFragment(activity?.supportFragmentManager!!)
        adapter.addFragment(ProductListFragment(merchant), getString(R.string.offer_title))
        adapter.addFragment(FeedFragment(merchant), getString(R.string.feed_title))
        adapter.addFragment(ContactFragment(merchant), getString(R.string.contact_title))

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_cart_outline)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_comment_quote_outline)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_account_box_outline)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_merchant_detail, menu)

//        isFavorite = merchant.favoriteId.contains(FirebaseAuth.getInstance().currentUser?.uid)
        menu.getItem(0).setIcon(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_outline)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_favorite -> {
                isFavorite = !isFavorite
                item.setIcon(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_outline)
                return true
            }
            android.R.id.home -> findNavController().popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }
}