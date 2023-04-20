package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.kilomobi.washy.R
import com.kilomobi.washy.databinding.FragmentWebclientLayoutBinding

class TermOfServiceFragment : FragmentEmptyView(R.layout.fragment_webclient_layout) {

    private lateinit var binding: FragmentWebclientLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebclientLayoutBinding.bind(view)

        binding.webview.webViewClient = WebViewClient()
        savedInstanceState?.getString("url", "")?.let { binding.webview.loadUrl(it) }
    }
}