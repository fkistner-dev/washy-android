package com.kilomobi.washy.fragment

import android.os.Bundle
import com.firebase.ui.auth.AuthUI
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
                .setIsSmartLockEnabled(false)
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.LoginTheme)
                .setTosAndPrivacyPolicyUrls(getString(R.string.tos_url), getString(R.string.privacy_url))
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }

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