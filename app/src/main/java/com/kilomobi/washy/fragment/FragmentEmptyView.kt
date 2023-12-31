/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.kilomobi.washy.R
import com.kilomobi.washy.util.WashyAuth

abstract class FragmentEmptyView(private val resourceInt: Int) : BaseFragment() {

    // Return class name, useful for debug purpose
    companion object {
        val TAG: String? = this::class.java.canonicalName
    }

    // Manage stack with Navigation Component
    var currentView: View? = null
    var viewIsCreated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (currentView == null)
            currentView = inflater.inflate(resourceInt, container, false)

        return currentView
    }

    fun displayEmptyView(text: String = getString(R.string.no_data_text)) {
        view?.findViewById<RelativeLayout>(R.id.empty_view_container)?.visibility = View.VISIBLE
        text.let { view?.findViewById<TextView>(R.id.empty_view_text)?.text = it }
    }

    fun hideEmptyView() {
        view?.findViewById<RelativeLayout>(R.id.empty_view_container)?.visibility = View.GONE
        view?.findViewById<TextView>(R.id.empty_view_text)?.visibility = View.GONE
    }

    fun isConnected(requireSnack: Boolean = false) : Boolean {
        return if (!WashyAuth.getUid().isNullOrBlank()) {
            true
        } else if (requireSnack) {
            Snackbar.make(requireView(), R.string.common_feature_require_authentication, Snackbar.LENGTH_LONG).show()
            false
        } else {
            false
        }
    }
}