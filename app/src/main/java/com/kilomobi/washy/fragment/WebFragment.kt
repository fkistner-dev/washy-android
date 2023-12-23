/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.kilomobi.washy.R
import com.kilomobi.washy.databinding.FragmentWebLayoutBinding

class WebFragment : FragmentEmptyView(R.layout.fragment_web_layout) {

    private lateinit var binding: FragmentWebLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebLayoutBinding.bind(view)

        binding.webview.webViewClient = WebViewClient()
        savedInstanceState?.getString("url", "")?.let { binding.webview.loadUrl(it) }
    }
}