package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R

abstract class FragmentEmptyView(private val resourceInt: Int) : Fragment() {

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

    fun isConnected() : Boolean {
        return if (!FirebaseAuth.getInstance().uid.isNullOrBlank()) {
            true
        } else {
            Snackbar.make(requireView(), R.string.common_feature_require_authentication, Snackbar.LENGTH_LONG).show()
            false
        }
    }
}