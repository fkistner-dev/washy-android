package com.kilomobi.washy.fragment

import android.content.Context
import android.preference.PreferenceManager
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kilomobi.washy.R

class StartFragment : FragmentEmptyView() {
    companion object {
        const val COMPLETED_ONBOARDING_PREF_NAME = "COMPLETED_ONBOARDING_PREF_NAME"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.startFragment, true)
                .build()
            // Check if we need to display our OnboardingSupportFragment
            if (!getBoolean(COMPLETED_ONBOARDING_PREF_NAME, false)) {
                // The user hasn't seen the OnboardingSupportFragment yet, so show it
                findNavController().navigate(R.id.action_startFragment_to_onboardingFragment, null, navOptions)
            } else {
                findNavController().navigate(R.id.action_startFragment_to_homeFragment, null, navOptions)
            }
        }
    }
}