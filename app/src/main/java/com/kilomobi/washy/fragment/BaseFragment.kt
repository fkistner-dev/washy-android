/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

open class BaseFragment : Fragment() {
    open fun navigate(view: View?, @IdRes destination: Int, args: Bundle?, navOptions: NavOptions? = null, navExtra: Navigator.Extras? = null) {
        try {
            // Best way to ensure proper navigation is to rely on view, otherwise get generic navController
            if (view != null) {
                findNavController(view).navigate(destination, args, navOptions, navExtra)
            } else {
                findNavController().navigate(destination, args, navOptions, navExtra)
            }
        } catch (e: IllegalArgumentException) {
            Log.e("Base", "Multiple navigation attempts handled.")
        }
    }
}