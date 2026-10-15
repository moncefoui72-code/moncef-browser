package com.moncef.browser.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moncef.browser.domain.Tab

@Composable
fun TabGridScreen(
    tabs: List<Tab>,
    currentTabId: String,
    onTabSelected: (Tab) -> Unit,
    onTabClosed: (Tab) -> Unit,
    onNewTab: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "التبويبات (${tabs.size})", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = onNewTab) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tabs) { tab ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clickable { onTabSelected(tab) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (tab.id == currentTabId) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tab.title,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(
                                onClick = { onTabClosed(tab) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                        }
                        Text(
                            text = tab.url,
                            maxLines = 2,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
