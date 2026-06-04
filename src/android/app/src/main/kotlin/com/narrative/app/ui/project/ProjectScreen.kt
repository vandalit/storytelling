package com.narrative.app.ui.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.CardType
import com.narrative.app.ui.components.NarrativeFab
import com.narrative.app.ui.theme.*

enum class ProjectView { MAZO, FLIPBOX, TIMELINE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    projectId: String,
    onBack: () -> Unit,
    onCardClick: (String) -> Unit,
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    LaunchedEffect(projectId) { viewModel.loadProject(projectId) }

    val state by viewModel.uiState.collectAsState()
    var activeView by remember { mutableStateOf(ProjectView.MAZO) }
    var activeFilter by remember { mutableStateOf<CardType?>(null) }

    Scaffold(
        containerColor = Background,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(state.project?.nombre ?: "", color = TextPrimary) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextMuted)
                        }
                    },
                    actions = {
                        // View switcher tabs
                        ProjectView.entries.forEach { view ->
                            val selected = activeView == view
                            TextButton(
                                onClick = { activeView = view },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = if (selected) Primary else TextMuted
                                ),
                            ) {
                                Text(
                                    view.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
                )
                // Filter chips
                if (activeView == ProjectView.MAZO) {
                    val types = state.cards.map { it.tipo }.distinct()
                    FilterChipRow(
                        types = types,
                        active = activeFilter,
                        onSelect = { activeFilter = if (activeFilter == it) null else it },
                    )
                }
            }
        },
        floatingActionButton = { NarrativeFab(onClick = { /* TODO nueva card en proyecto */ }) },
    ) { padding ->
        val filtered = if (activeFilter != null) state.cards.filter { it.tipo == activeFilter } else state.cards

        when (activeView) {
            ProjectView.MAZO -> MazoView(
                cards = filtered,
                onCardClick = onCardClick,
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
            )
            ProjectView.FLIPBOX -> FlipboxPlaceholder(Modifier.padding(padding))
            ProjectView.TIMELINE -> TimelinePlaceholder(Modifier.padding(padding))
        }
    }
}

@Composable
private fun MazoView(cards: List<Card>, onCardClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp, 10.dp, 10.dp, 100.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp,
    ) {
        items(cards, key = { it.id }) { card ->
            MazoCardItem(card = card, onClick = { onCardClick(card.id) })
        }
    }
}

@Composable
private fun MazoCardItem(card: Card, onClick: () -> Unit) {
    val typeColor = cardTypeColor(card.tipo)
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                Surface(shape = RoundedCornerShape(4.dp), color = typeColor.copy(alpha = 0.15f)) {
                    Text(
                        card.tipo.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = typeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    )
                }
            }
            card.titulo?.let {
                Spacer(Modifier.height(6.dp))
                Text(it, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                card.cuerpo,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
            )
            card.escena?.beatEtiqueta?.let {
                Spacer(Modifier.height(6.dp))
                Text("● $it", style = MaterialTheme.typography.labelSmall, color = Accent)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChipRow(types: List<CardType>, active: CardType?, onSelect: (CardType) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        types.forEach { tipo ->
            val selected = active == tipo
            FilterChip(
                selected = selected,
                onClick = { onSelect(tipo) },
                label = { Text(tipo.label, style = MaterialTheme.typography.labelSmall) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGlow,
                    selectedLabelColor = Primary,
                ),
            )
        }
    }
}

@Composable private fun FlipboxPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Text("Flipbox — próxima iteración", color = TextMuted, modifier = Modifier.padding(24.dp))
    }
}

@Composable private fun TimelinePlaceholder(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Text("Timeline — próxima iteración", color = TextMuted, modifier = Modifier.padding(24.dp))
    }
}

fun cardTypeColor(tipo: CardType) = when (tipo) {
    CardType.BLANCA        -> TypeBlanca
    CardType.PREMISA       -> TypePremisa
    CardType.PERSONAJE     -> TypePersonaje
    CardType.ESCENA        -> TypeEscena
    CardType.LOCACION      -> TypeLocacion
    CardType.WORLDBUILDING -> TypeWorldbuilding
    CardType.CONFLICTO     -> TypeConflicto
    CardType.PROP          -> TypeProp
    CardType.BEAT          -> TypeBeat
    CardType.NOTA          -> TypeNota
}
