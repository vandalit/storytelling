package com.narrative.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.Project
import com.narrative.app.ui.components.NarrativeFab
import com.narrative.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProjectClick: (String) -> Unit,
    onInboxClick: () -> Unit,
    onCardClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Surface,
                drawerContentColor = TextPrimary,
            ) {
                // Off-canvas PKM — placeholder para siguiente iteración
                Text(
                    text = "narrative",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Primary,
                    modifier = Modifier.padding(24.dp),
                )
                HorizontalDivider(color = Border)
                Spacer(Modifier.height(8.dp))
                NavigationDrawerItem(
                    label = { Text("Inbox", color = TextPrimary) },
                    selected = false,
                    onClick = onInboxClick,
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                )
                state.projects.forEach { project ->
                    NavigationDrawerItem(
                        label = { Text(project.nombre, color = TextPrimary) },
                        selected = false,
                        onClick = { onProjectClick(project.id) },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                    )
                }
            }
        },
    ) {
        Scaffold(
            containerColor = Background,
            topBar = {
                TopAppBar(
                    title = { Text("narrative", color = Primary, style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = { /* abrir drawer */ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = TextMuted)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
                )
            },
            floatingActionButton = {
                NarrativeFab(onClick = { /* TODO: sheet nueva card / proyecto */ })
            },
        ) { padding ->

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
                return@Scaffold
            }

            LazyColumn(
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = 100.dp, start = 16.dp, end = 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Proyectos
                item {
                    SectionLabel("Proyectos")
                    Spacer(Modifier.height(8.dp))
                }
                items(state.projects, key = { it.id }) { project ->
                    ProjectCard(project = project, onClick = { onProjectClick(project.id) })
                }

                // Inbox
                item {
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SectionLabel("Inbox")
                        Spacer(Modifier.width(8.dp))
                        Badge(containerColor = PrimaryDim, contentColor = Primary) {
                            Text("${state.inboxCards.size}")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                items(state.inboxCards.take(3), key = { it.id }) { card ->
                    InboxCardItem(card = card, onClick = { onCardClick(card.id) })
                }
                if (state.inboxCards.size > 3) {
                    item {
                        TextButton(onClick = onInboxClick) {
                            Text("Ver todos (${state.inboxCards.size}) →", color = Primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(project: Project, onClick: () -> Unit) {
    val projectColor = runCatching { Color(android.graphics.Color.parseColor(project.color)) }
        .getOrDefault(Primary)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = projectColor.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("◆", color = projectColor, style = MaterialTheme.typography.bodyLarge)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(project.nombre, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text(
                    "${project.tipo.label} · ${project.division.label}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                )
            }
            Text("›", color = TextDim, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun InboxCardItem(card: Card, onClick: () -> Unit) {
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = TextDim,
    )
}
