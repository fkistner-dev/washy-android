package com.kilomobi.washy.fragment

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kilomobi.washy.R

abstract class FragmentEmptyView : Fragment() {
    fun displayEmptyView(text: String = getString(R.string.no_data_text)) {
        view?.findViewById<RelativeLayout>(R.id.empty_view_container)?.visibility = View.VISIBLE
        text.let { view?.findViewById<TextView>(R.id.empty_view_text)?.text = it }
    }
}