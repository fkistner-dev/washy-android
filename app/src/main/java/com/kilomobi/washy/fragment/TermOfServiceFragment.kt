package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.kilomobi.washy.R

class TermOfServiceFragment : Fragment() {
    private var privacyWebview: WebView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_visit_tos_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        privacyWebview = view.findViewById<View>(R.id.webview) as WebView
        privacyWebview?.loadUrl(getString(R.string.privacy_url))
        privacyWebview?.settings?.javaScriptCanOpenWindowsAutomatically = true
    }
}
