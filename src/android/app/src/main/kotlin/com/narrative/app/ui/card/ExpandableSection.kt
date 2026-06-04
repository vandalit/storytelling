package com.narrative.app.ui.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.narrative.app.domain.model.EscenaData
import com.narrative.app.domain.model.PersonajeData
import com.narrative.app.ui.theme.*

// Secciones on-demand colapsables — el patrón central de la UX

@Composable
fun PersonajeSection(data: PersonajeData?, onChange: (PersonajeData) -> Unit) {
    val p = data ?: PersonajeData()

    ExpandableSection(title = "Psicología") {
        NarrativeField("Rol",               p.rol)              { onChange(p.copy(rol = it)) }
        NarrativeField("Motivación",        p.motivacion)       { onChange(p.copy(motivacion = it)) }
        NarrativeField("Conflicto interno", p.conflictoInterno) { onChange(p.copy(conflictoInterno = it)) }
        NarrativeField("Miedo",             p.miedo)            { onChange(p.copy(miedo = it)) }
        NarrativeField("Deseo",             p.deseo)            { onChange(p.copy(deseo = it)) }
        NarrativeField("Defecto",           p.defecto)          { onChange(p.copy(defecto = it)) }
    }
    ExpandableSection(title = "Biografía") {
        NarrativeField("Backstory", p.backstory) { onChange(p.copy(backstory = it)) }
        NarrativeField("Hábitos",   p.habitos)   { onChange(p.copy(habitos = it)) }
        NarrativeField("Apariencia",p.apariencia){ onChange(p.copy(apariencia = it)) }
    }
    ExpandableSection(title = "Arco narrativo") {
        NarrativeField("Estado inicial",    p.estadoInicial)   { onChange(p.copy(estadoInicial = it)) }
        NarrativeField("Estado final",      p.estadoFinal)     { onChange(p.copy(estadoFinal = it)) }
        NarrativeField("Cambio emocional",  p.cambioEmocional) { onChange(p.copy(cambioEmocional = it)) }
    }
}

@Composable
fun EscenaSection(data: EscenaData?, onChange: (EscenaData) -> Unit) {
    val e = data ?: EscenaData()

    ExpandableSection(title = "Dramaturgia") {
        NarrativeField("Objetivo",          e.objetivo)         { onChange(e.copy(objetivo = it)) }
        NarrativeField("Conflicto",         e.conflicto)        { onChange(e.copy(conflicto = it)) }
        NarrativeField("Stake",             e.stake)            { onChange(e.copy(stake = it)) }
        NarrativeField("Turning point",     e.turningPoint)     { onChange(e.copy(turningPoint = it)) }
        NarrativeField("Momento memorable", e.momentoMemorable) { onChange(e.copy(momentoMemorable = it)) }
        NarrativeField("Mood",              e.mood)             { onChange(e.copy(mood = it)) }
    }
    ExpandableSection(title = "Beat en el arco") {
        // Slider beat_posicion
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            val pos = e.beatPosicion ?: 0.5f
            Text("${(pos * 100).toInt()}%", style = MaterialTheme.typography.headlineMedium, color = Primary)
            Slider(
                value = pos,
                onValueChange = { onChange(e.copy(beatPosicion = it)) },
                colors = SliderDefaults.colors(activeTrackColor = Primary, thumbColor = Primary),
            )
            Text("Posición en el arco (0% inicio → 100% final)", style = MaterialTheme.typography.labelSmall, color = TextDim)
        }
        NarrativeField("Etiqueta de beat",       e.beatEtiqueta)          { onChange(e.copy(beatEtiqueta = it)) }
        NarrativeField("Estado emocional antes",  e.cambioEmocionalAntes)  { onChange(e.copy(cambioEmocionalAntes = it)) }
        NarrativeField("Estado emocional después",e.cambioEmocionalDespues){ onChange(e.copy(cambioEmocionalDespues = it)) }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = e.esSetup, onCheckedChange = { onChange(e.copy(esSetup = it)) }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                Text("Setup", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = e.esPayoff, onCheckedChange = { onChange(e.copy(esPayoff = it)) }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                Text("Payoff", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ExpandableSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = if (expanded) Primary else TextMuted)
            Text(if (expanded) "▴" else "▾", color = if (expanded) Primary else TextDim)
        }
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content,
            )
        }
        HorizontalDivider(color = Border)
    }
}

@Composable
fun NarrativeField(label: String, value: String?, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = TextDim)
        OutlinedTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
            colors = narrativeTextFieldColors(),
            minLines = 1, maxLines = 4,
        )
    }
}
