package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.kilomobi.washy.R
import com.kilomobi.washy.databinding.ActivityVisitTosLayoutBinding

class TermOfServiceFragment : FragmentEmptyView(R.layout.activity_visit_tos_layout) {

    private lateinit var binding: ActivityVisitTosLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityVisitTosLayoutBinding.bind(view)

        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl(getString(R.string.privacy_url))
    }
}