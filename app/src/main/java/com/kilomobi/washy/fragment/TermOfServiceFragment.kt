package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.kilomobi.washy.R
import kotlinx.android.synthetic.main.activity_visit_tos_layout.*


class TermOfServiceFragment : FragmentEmptyView(R.layout.activity_visit_tos_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webview.webViewClient = WebViewClient()
        webview?.loadUrl(getString(R.string.privacy_url))
    }
}