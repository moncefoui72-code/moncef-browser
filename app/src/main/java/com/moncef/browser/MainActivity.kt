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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moncef.browser.domain.SearchEngine
import com.moncef.browser.domain.Tab
import com.moncef.browser.ui.components.SearchEngineSelector
import com.moncef.browser.ui.components.SearchBar
import com.moncef.browser.ui.components.TabGridScreen
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
                    val tabs = remember { mutableStateListOf(Tab()) }
                    var currentTabId by remember { mutableStateOf(tabs.first().id) }
                    var showTabGrid by remember { mutableStateOf(false) }
                    var searchText by remember { mutableStateOf("") }
                    var selectedEngine by remember { mutableStateOf(SearchEngine.GOOGLE) }

                    val currentTab = tabs.find { it.id == currentTabId } ?: tabs.first()

                    Scaffold(
                        topBar = {
                            if (!showTabGrid) {
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
                                                value = if (searchText.isEmpty()) currentTab.url else searchText,
                                                onValueChange = { searchText = it },
                                                onSearch = {
                                                    val url = if (searchText.startsWith("http://") || searchText.startsWith("https://")) {
                                                        searchText
                                                    } else if (searchText.contains(".")) {
                                                        "https://$searchText"
                                                    } else {
                                                        "${selectedEngine.searchUrl}$searchText"
                                                    }
                                                    currentTab.url = url
                                                    currentTab.title = searchText
                                                    searchText = ""
                                                    showTabGrid = false
                                                }
                                            )
                                        }
                                        IconButton(onClick = { showTabGrid = true }) {
                                            Icon(Icons.Default.Menu, contentDescription = null)
                                        }
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
                            if (showTabGrid) {
                                TabGridScreen(
                                    tabs = tabs,
                                    currentTabId = currentTabId,
                                    onTabSelected = { tab ->
                                        currentTabId = tab.id
                                        searchText = ""
                                        showTabGrid = false
                                    },
                                    onTabClosed = { tab ->
                                        if (tabs.size > 1) {
                                            tabs.remove(tab)
                                            if (currentTabId == tab.id) {
                                                currentTabId = tabs.last().id
                                            }
                                        }
                                    },
                                    onNewTab = {
                                        val newTab = Tab()
                                        tabs.add(newTab)
                                        currentTabId = newTab.id
                                        searchText = ""
                                        showTabGrid = false
                                    }
                                )
                            } else {
                                BrowserWebView(
                                    url = currentTab.url,
                                    onUrlChanged = { newUrl ->
                                        currentTab.url = newUrl
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
}
