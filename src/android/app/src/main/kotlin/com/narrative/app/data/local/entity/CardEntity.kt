package com.narrative.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.CardEstado
import com.narrative.app.domain.model.CardType
import com.narrative.app.domain.model.EscenaData
import com.narrative.app.domain.model.PersonajeData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Los campos extendidos (personaje, escena) se serializan como JSON en columnas TEXT.
// Justificación: son objetos opcionales con ~15 campos nullable cada uno.
// Una tabla por tipo generaría 2 JOINs extra en cada query sin beneficio real para el MVP.

@Entity(
    tableName = "cards",
    indices = [Index("proyecto_id"), Index("tipo"), Index("estado")]
)
data class CardEntity(
    @PrimaryKey val id: String,
    val titulo: String?,
    val cuerpo: String,
    val tipo: String,               // CardType.name
    val proyecto_id: String?,
    val contenedor_id: String?,
    val estado: String,             // CardEstado.name
    val color_label: String?,
    val fecha_creacion: Long,
    val fecha_edicion: Long,
    val orden: Int?,
    val personaje_json: String?,    // PersonajeData serializado
    val escena_json: String?,       // EscenaData serializado
) {
    fun toDomain(tags: List<String>): Card = Card(
        id             = id,
        titulo         = titulo,
        cuerpo         = cuerpo,
        tipo           = CardType.valueOf(tipo),
        proyectoId     = proyecto_id,
        contenedorId   = contenedor_id,
        tags           = tags,
        estado         = CardEstado.valueOf(estado),
        colorLabel     = color_label,
        fechaCreacion  = fecha_creacion,
        fechaEdicion   = fecha_edicion,
        orden          = orden,
        personaje      = personaje_json?.let { Json.decodeFromString(it) },
        escena         = escena_json?.let { Json.decodeFromString(it) },
    )
}

fun Card.toEntity(): CardEntity = CardEntity(
    id             = id,
    titulo         = titulo,
    cuerpo         = cuerpo,
    tipo           = tipo.name,
    proyecto_id    = proyectoId,
    contenedor_id  = contenedorId,
    estado         = estado.name,
    color_label    = colorLabel,
    fecha_creacion = fechaCreacion,
    fecha_edicion  = fechaEdicion,
    orden          = orden,
    personaje_json = personaje?.let { Json.encodeToString(it) },
    escena_json    = escena?.let { Json.encodeToString(it) },
)
