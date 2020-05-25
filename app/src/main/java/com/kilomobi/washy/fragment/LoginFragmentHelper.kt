package com.kilomobi.washy.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.kilomobi.washy.R

abstract class LoginFragmentHelper : FirebaseUIFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_local_car_wash)
                .setTheme(R.style.LoginTheme)
                .setTosAndPrivacyPolicyUrls(getString(R.string.tos_url), getString(R.string.privacy_url))
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                findNavController().navigate(R.id.action_identification_fragment_to_map_fragment)

                Toast.makeText(requireContext(), "Sign in success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Sign in failed", Toast.LENGTH_LONG).show()
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }

    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(requireContext())
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }

}