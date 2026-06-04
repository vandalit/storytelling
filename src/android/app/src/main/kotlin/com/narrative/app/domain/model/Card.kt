package com.narrative.app.domain.model

import kotlinx.serialization.Serializable

data class Card(
    val id: String,
    val titulo: String?,
    val cuerpo: String,
    val tipo: CardType = CardType.BLANCA,
    val proyectoId: String?,
    val contenedorId: String?,
    val tags: List<String> = emptyList(),
    val estado: CardEstado = CardEstado.ACTIVA,
    val colorLabel: String? = null,
    val fechaCreacion: Long,
    val fechaEdicion: Long,
    val orden: Int? = null,
    val personaje: PersonajeData? = null,
    val escena: EscenaData? = null,
)

enum class CardType(val label: String) {
    BLANCA("Sin tipo"),
    PREMISA("Premisa"),
    PERSONAJE("Personaje"),
    ESCENA("Escena"),
    LOCACION("Locación"),
    WORLDBUILDING("Worldbuilding"),
    CONFLICTO("Conflicto"),
    PROP("Prop"),
    BEAT("Beat"),
    NOTA("Nota"),
}

enum class CardEstado { BORRADOR, ACTIVA, ARCHIVADA }

@Serializable
data class PersonajeData(
    val rol: String? = null,
    val motivacion: String? = null,
    val conflictoInterno: String? = null,
    val miedo: String? = null,
    val deseo: String? = null,
    val defecto: String? = null,
    val backstory: String? = null,
    val habitos: String? = null,
    val apariencia: String? = null,
    val imagenUrl: String? = null,
    val estadoInicial: String? = null,
    val estadoFinal: String? = null,
    val cambioEmocional: String? = null,
)

@Serializable
data class EscenaData(
    val tipoEscena: String? = null,
    val mood: String? = null,
    val objetivo: String? = null,
    val conflicto: String? = null,
    val stake: String? = null,
    val turningPoint: String? = null,
    val momentoMemorable: String? = null,
    val descripcion: String? = null,
    // 0.0 = inicio del arco, 1.0 = final
    val beatPosicion: Float? = null,
    val beatEtiqueta: String? = null,
    val cambioEmocionalAntes: String? = null,
    val cambioEmocionalDespues: String? = null,
    val esSetup: Boolean = false,
    val esPayoff: Boolean = false,
    val numeroActo: Int? = null,
    val duracionEstimada: String? = null,
    val notasProduccion: String? = null,
)
