package com.kilomobi.washy.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.UserListener

class IdentificationFragment : LoginFragmentHelper() {

    private lateinit var userListener: UserListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        try {
            userListener = context as UserListener
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        return view
    }

    // [START auth_fui_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.let { userListener.onAuthenticationConnected(it) }
            } else {
                userListener.onAuthenticationCancel()
            }
        }
        findNavController().popBackStack()
    }
    // [END auth_fui_result]
}