package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate

class HomeFragment : FragmentEmptyView(R.layout.layout_home) {

    private lateinit var mainActivityDelegate: MainActivityDelegate

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

        arguments?.getBoolean("isFullscreen")?.let { mainActivityDelegate.setFullscreen(it) }

        return currentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            if (activity?.supportFragmentManager?.findFragmentByTag("feed") == null) {
                val fragmentTransaction: FragmentTransaction? =
                    activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.add(R.id.listFeed, FeedViewPagerFragment(), "feed")
                //fragmentTransaction?.add(R.id.listMap, MapFragment(),"map")
                fragmentTransaction?.add(R.id.listMerchant, MerchantListFragment(), "merchant")

                fragmentTransaction?.commit()
            } else {
                Log.e(TAG, "I KNEW IT")
            }

            viewIsCreated = true
        }
    }
}