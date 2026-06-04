package com.narrative.app.ui.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.narrative.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    cardId: String,
    onBack: () -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(cardId) { viewModel.loadCard(cardId) }
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(state.card?.titulo ?: "Card", color = TextPrimary, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextMuted)
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.save(); onBack() }) {
                        Text("Guardar", color = Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
            )
        },
    ) { padding ->
        val card = state.card ?: return@Scaffold

        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(bottom = 48.dp),
        ) {
            // Título
            DetailSection {
                DetailLabel("Título")
                OutlinedTextField(
                    value = state.titulo,
                    onValueChange = viewModel::onTituloChange,
                    placeholder = { Text("Sin título", color = TextDim) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = narrativeTextFieldColors(),
                    textStyle = MaterialTheme.typography.titleLarge.copy(color = TextPrimary),
                )
            }

            // Cuerpo
            DetailSection {
                DetailLabel("Contenido")
                OutlinedTextField(
                    value = state.cuerpo,
                    onValueChange = viewModel::onCuerpoChange,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                    colors = narrativeTextFieldColors(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextPrimary),
                )
            }

            // Tipo
            DetailSection {
                DetailLabel("Tipo de card")
                // TODO: type selector grid — próxima iteración
                Text(card.tipo.label, color = Primary, style = MaterialTheme.typography.bodyMedium)
            }

            // Tags
            DetailSection {
                DetailLabel("Tags")
                Text(
                    if (card.tags.isEmpty()) "Sin tags — escribe #tag para agregar"
                    else card.tags.joinToString(" "),
                    color = if (card.tags.isEmpty()) TextDim else Primary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            // Secciones on-demand: personaje
            if (card.tipo.name == "PERSONAJE") {
                PersonajeSection(
                    data = card.personaje,
                    onChange = viewModel::onPersonajeChange,
                )
            }

            // Secciones on-demand: escena
            if (card.tipo.name == "ESCENA") {
                EscenaSection(
                    data = card.escena,
                    onChange = viewModel::onEscenaChange,
                )
            }

            // Meta
            DetailSection {
                Text(
                    "Creada ${card.fechaCreacion} · Editada ${card.fechaEdicion}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDim,
                )
            }
        }
    }
}

@Composable
fun DetailSection(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        content = content,
    )
    HorizontalDivider(color = Border)
}

@Composable
fun DetailLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = TextDim,
    )
}

@Composable
fun narrativeTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = Primary,
    unfocusedBorderColor = Border,
    cursorColor          = Primary,
)
