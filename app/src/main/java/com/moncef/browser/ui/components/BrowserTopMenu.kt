package com.moncef.browser.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun BrowserTopMenu(
    isDesktopMode: Boolean,
    onToggleDesktopMode: () -> Unit,
    onShareClicked: () -> Unit,
    onNewIncognitoTab: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(if (isDesktopMode) "وضع الهاتف" else "وضع سطح المكتب") },
                onClick = {
                    onToggleDesktopMode()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("مشاركة الصفحة") },
                onClick = {
                    onShareClicked()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("تبويب خفي جديد") },
                onClick = {
                    onNewIncognitoTab()
                    expanded = false
                }
            )
        }
    }
}
