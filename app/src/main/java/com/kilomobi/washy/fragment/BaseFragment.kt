package com.kilomobi.washy.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

class BaseFragment : Fragment() {
    fun navigate(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
        with(findNavController()) {
            // 1
            currentDestination?.getAction(destination.actionId)
                ?.let {
                    navigate(destination, extraInfo) //2 }
                }
        }
}