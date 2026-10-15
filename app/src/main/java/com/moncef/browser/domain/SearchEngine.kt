package com.moncef.browser.domain

enum class SearchEngine(val title: String, val searchUrl: String) {
    GOOGLE("Google", "https://www.google.com/search?q="),
    BING("Bing", "https://www.bing.com/search?q="),
    YAHOO("Yahoo", "https://search.yahoo.com/search?p="),
    DUCKDUCKGO("DuckDuckGo", "https://duckduckgo.com/?q="),
    YANDEX("Yandex", "https://yandex.com/search/?text="),
    AOL("AOL", "https://search.aol.com/aol/search?q=")
}
