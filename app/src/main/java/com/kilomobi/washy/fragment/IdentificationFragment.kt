package com.kilomobi.washy.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kilomobi.washy.activity.UserListener
import com.kilomobi.washy.util.WashyAuth

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = WashyAuth.getUid()
                user?.let { userListener.onAuthenticationConnected(it) }
            } else {
                userListener.onAuthenticationCancel()
            }
        }
        findNavController().popBackStack()
    }
}