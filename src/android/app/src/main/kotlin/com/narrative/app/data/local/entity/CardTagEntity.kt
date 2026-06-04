package com.narrative.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

// Tabla de join para tags — permite queries bidireccionales eficientes:
// "cards con este tag" (arrayContains equivalente en SQLite)
// "todos los tags de una card"

@Entity(
    tableName = "card_tags",
    primaryKeys = ["card_id", "tag"],
    foreignKeys = [ForeignKey(
        entity = CardEntity::class,
        parentColumns = ["id"],
        childColumns = ["card_id"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("card_id"), Index("tag")],
)
data class CardTagEntity(
    val card_id: String,
    val tag: String,
)
