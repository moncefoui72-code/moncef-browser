package com.moncef.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.moncef.browser.domain.SearchEngine
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
                    val searchEngine = SearchEngine.GOOGLE

                    Scaffold(
                        topBar = {
                            SearchBar(
                                value = searchText,
                                onValueChange = { searchText = it },
                                onSearch = {
                                    if (searchText.startsWith("http://") || searchText.startsWith("https://")) {
                                        currentUrl = searchText
                                    } else if (searchText.contains(".")) {
                                        currentUrl = "https://$searchText"
                                    } else {
                                        currentUrl = "${searchEngine.searchUrl}$searchText"
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            BrowserWebView(
                                url = currentUrl,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
