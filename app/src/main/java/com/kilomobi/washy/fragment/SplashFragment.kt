package com.kilomobi.washy.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.layout_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            context?.let {
//                val user = FirebaseAuth.getInstance().currentUser
//                if (user != null) findNavController().navigate(R.id.action_splash_fragment_to_home_fragment) else findNavController().navigate(R.id.action_splash_fragment_to_home_fragment) // todo replace to introStart
            }
        }, 2500)
    }
}