package com.moncef.browser.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moncef.browser.domain.SearchEngine

@Composable
fun SearchEngineSelector(
    selectedEngine: SearchEngine,
    onEngineSelected: (SearchEngine) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 4.dp)) {
        Text(
            text = selectedEngine.title,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SearchEngine.values().forEach { engine ->
                DropdownMenuItem(
                    text = { Text(engine.title) },
                    onClick = {
                        onEngineSelected(engine)
                        expanded = false
                    }
                )
            }
        }
    }
}
