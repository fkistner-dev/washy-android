/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.content.Context
import android.preference.PreferenceManager
import androidx.navigation.NavOptions
import com.kilomobi.washy.R

class StartFragment : FragmentEmptyView(R.layout.fragment_empty) {
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
                navigate(currentView, R.id.action_startFragment_to_onboardingFragment, null, navOptions)
            } else {
                navigate(currentView, R.id.action_startFragment_to_homeFragment, null, navOptions)
            }
        }
    }
}