package com.narrative.app.ui.inbox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.narrative.app.domain.model.Card
import com.narrative.app.ui.components.NarrativeFab
import com.narrative.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    onBack: () -> Unit,
    onCardClick: (String) -> Unit,
    viewModel: InboxViewModel = hiltViewModel(),
) {
    val cards by viewModel.inboxCards.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Inbox", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextMuted)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
            )
        },
        floatingActionButton = { NarrativeFab(onClick = { /* TODO nueva card */ }) },
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 12.dp,
                bottom = 100.dp, start = 12.dp, end = 12.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(cards, key = { it.id }) { card ->
                InboxItem(card = card, onClick = { onCardClick(card.id) })
            }
        }
    }
}

@Composable
private fun InboxItem(card: Card, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            card.titulo?.let {
                Text(it, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
            }
            Text(
                card.cuerpo,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            if (card.tags.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    card.tags.take(4).forEach { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(tag, style = MaterialTheme.typography.labelSmall, color = Primary) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = PrimaryGlow),
                            border = SuggestionChipDefaults.suggestionChipBorder(enabled = true, borderColor = PrimaryDim),
                        )
                    }
                }
            }
        }
    }
}
