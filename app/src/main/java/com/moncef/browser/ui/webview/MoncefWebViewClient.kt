package com.moncef.browser.ui.webview

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

class MoncefWebViewClient(
    private val onUrlChanged: (String) -> Unit
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.url?.toString()?.let { url ->
            view?.loadUrl(url)
            onUrlChanged(url)
        }
        return true
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        val url = request?.url.toString().lowercase()
        val adKeywords = arrayOf("ads", "doubleclick", "adservice", "tracker", "analytics")
        for (keyword in adKeywords) {
            if (url.contains(keyword)) {
                return WebResourceResponse("text/plain", "utf-8", null)
            }
        }
        return super.shouldInterceptRequest(view, request)
    }
}
