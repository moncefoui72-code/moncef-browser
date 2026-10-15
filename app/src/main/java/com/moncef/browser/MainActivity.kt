package com.moncef.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moncef.browser.domain.SearchEngine
import com.moncef.browser.ui.components.SearchEngineSelector
import com.moncef.browser.ui.components.SearchBar
import com.moncef.browser.ui.theme.MoncefBrowserTheme
import com.moncef.browser.ui.webview.BrowserWebView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoncefBrowserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentUrl by remember { mutableStateOf("https://www.google.com") }
                    var searchText by remember { mutableStateOf("") }
                    var selectedEngine by remember { mutableStateOf(SearchEngine.GOOGLE) }

                    Scaffold(
                        topBar = {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SearchEngineSelector(
                                        selectedEngine = selectedEngine,
                                        onEngineSelected = { selectedEngine = it }
                                    )
                                    Box(modifier = Modifier.weight(1f)) {
                                        SearchBar(
                                            value = searchText,
                                            onValueChange = { searchText = it },
                                            onSearch = {
                                                if (searchText.startsWith("http://") || searchText.startsWith("https://")) {
                                                    currentUrl = searchText
                                                } else if (searchText.contains(".")) {
                                                    currentUrl = "https://$searchText"
                                                } else {
                                                    currentUrl = "${selectedEngine.searchUrl}$searchText"
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            BrowserWebView(
                                url = currentUrl,
                                onUrlChanged = { newUrl ->
                                    currentUrl = newUrl
                                    searchText = newUrl
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
