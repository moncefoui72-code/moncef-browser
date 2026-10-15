package com.moncef.browser.domain

import java.util.UUID

data class Tab(
    val id: String = UUID.randomUUID().toString(),
    var url: String = "https://www.google.com",
    var title: String = "Google",
    var isIncognito: Boolean = false
)
