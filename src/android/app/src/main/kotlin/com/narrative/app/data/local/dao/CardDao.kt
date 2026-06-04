package com.narrative.app.data.local.dao

import androidx.room.*
import com.narrative.app.data.local.entity.CardEntity
import com.narrative.app.data.local.entity.CardTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    // ── Queries ────────────────────────────────────────────────

    @Query("SELECT * FROM cards WHERE proyecto_id = :proyectoId ORDER BY fecha_edicion DESC")
    fun getProjectCards(proyectoId: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE proyecto_id IS NULL ORDER BY fecha_edicion DESC")
    fun getInboxCards(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getCard(id: String): Flow<CardEntity?>

    @Query("SELECT * FROM cards WHERE tipo = :tipo AND proyecto_id = :proyectoId")
    fun getCardsByType(proyectoId: String, tipo: String): Flow<List<CardEntity>>

    // Escenas del proyecto con beat_posicion para el timeline
    @Query("""
        SELECT * FROM cards
        WHERE proyecto_id = :proyectoId AND tipo = 'ESCENA'
        ORDER BY CAST(json_extract(escena_json, '$.beatPosicion') AS REAL) ASC
    """)
    fun getScenesForTimeline(proyectoId: String): Flow<List<CardEntity>>

    // Cards que comparten al menos un tag con la card dada
    @Query("""
        SELECT DISTINCT c.* FROM cards c
        INNER JOIN card_tags ct ON c.id = ct.card_id
        WHERE ct.tag IN (SELECT tag FROM card_tags WHERE card_id = :cardId)
        AND c.id != :cardId
    """)
    fun getRelatedCards(cardId: String): Flow<List<CardEntity>>

    // Cards con un tag específico
    @Query("""
        SELECT c.* FROM cards c
        INNER JOIN card_tags ct ON c.id = ct.card_id
        WHERE ct.tag = :tag
        ORDER BY c.fecha_edicion DESC
    """)
    fun getCardsByTag(tag: String): Flow<List<CardEntity>>

    // Búsqueda full-text en título y cuerpo
    @Query("""
        SELECT * FROM cards
        WHERE (titulo LIKE '%' || :query || '%' OR cuerpo LIKE '%' || :query || '%')
        ORDER BY fecha_edicion DESC
        LIMIT 50
    """)
    fun searchCards(query: String): Flow<List<CardEntity>>

    // Tags de una card
    @Query("SELECT tag FROM card_tags WHERE card_id = :cardId")
    suspend fun getTagsForCard(cardId: String): List<String>

    @Query("SELECT tag FROM card_tags WHERE card_id = :cardId")
    fun getTagsForCardFlow(cardId: String): Flow<List<String>>

    // Todos los tags del sistema (PKM global)
    @Query("SELECT DISTINCT tag FROM card_tags ORDER BY tag ASC")
    fun getAllTags(): Flow<List<String>>

    // ── Mutations ──────────────────────────────────────────────

    @Upsert
    suspend fun upsertCard(card: CardEntity)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteCard(id: String)

    @Query("DELETE FROM card_tags WHERE card_id = :cardId")
    suspend fun deleteTags(cardId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<CardTagEntity>)

    @Transaction
    suspend fun updateTags(cardId: String, tags: List<String>) {
        deleteTags(cardId)
        insertTags(tags.map { CardTagEntity(card_id = cardId, tag = it) })
    }
}
