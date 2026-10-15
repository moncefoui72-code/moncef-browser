package com.moncef.browser.ui.webview

import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BrowserWebView(
    url: String,
    isDesktopMode: Boolean,
    onUrlChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                
                val cookieManager = CookieManager.getInstance()
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(this, false)

                webViewClient = MoncefWebViewClient(onUrlChanged)
                loadUrl(url)
            }
        },
        update = { webView ->
            if (isDesktopMode) {
                webView.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            } else {
                webView.settings.userAgentString = null
            }
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        }
    )
}
