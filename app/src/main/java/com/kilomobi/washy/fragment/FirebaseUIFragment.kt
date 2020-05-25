package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kilomobi.washy.R

abstract class FirebaseUIFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.activity_firebase_ui, container, false)
    companion object {
        internal const val RC_SIGN_IN = 123
    }
}