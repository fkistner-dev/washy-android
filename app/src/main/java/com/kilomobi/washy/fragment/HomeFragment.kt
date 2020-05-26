package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kilomobi.washy.merchant.MerchantListFragment
import com.kilomobi.washy.feed.FeedViewPagerFragment
import com.kilomobi.washy.MainActivityDelegate
import com.kilomobi.washy.R
import com.kilomobi.washy.util.initToolbar
import kotlinx.android.synthetic.main.layout_top_bar.*

class HomeFragment : Fragment() {
    private lateinit var mainActivityDelegate: MainActivityDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_home, container, false)

        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(toolbar, false)
        mainActivityDelegate.setupNavDrawer(toolbar)
        mainActivityDelegate.enableNavDrawer(true)

        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()

        fragmentTransaction?.add(R.id.listFeed, FeedViewPagerFragment(), "feed")
        fragmentTransaction?.add(R.id.listMap, MapFragment(),"map")
        fragmentTransaction?.add(R.id.listMerchant, MerchantListFragment(), "merchant")

        fragmentTransaction?.commit()
    }
}