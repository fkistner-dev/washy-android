/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.util

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import com.kilomobi.washy.R

class ChromeUtils {
    companion object {
        fun openChromeTab(context: Context, view: View, url: String) {
            val builder = CustomTabsIntent.Builder()
            val params = CustomTabColorSchemeParams.Builder()
            params.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            builder.setDefaultColorSchemeParams(params.build())
            builder.setShowTitle(true)

            builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            builder.setExitAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            val customBuilder = builder.build()
            val packageName = "com.android.chrome"

            if (context.isPackageInstalled(packageName)) {
                // if chrome is available use chrome custom tabs
                customBuilder.intent.setPackage(packageName)
                val urlUri = Uri.parse(url.ifEmpty { context.getString(R.string.privacy_url) })
                customBuilder.launchUrl(context, urlUri)
            } else {
                // if not available use WebView to launch the url
                findNavController(view).navigate(R.id.action_homeFragment_to_tosFragment, bundleOf(Pair("url", url)))
            }
        }

        private fun Context.isPackageInstalled(packageName: String): Boolean {
            // check if chrome is installed or not
            return try {
                packageManager.getPackageInfo(packageName, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }
}