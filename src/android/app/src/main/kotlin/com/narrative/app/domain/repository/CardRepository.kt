package com.narrative.app.domain.repository

import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.CardType
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getProjectCards(proyectoId: String): Flow<List<Card>>
    fun getInboxCards(): Flow<List<Card>>
    fun getCard(id: String): Flow<Card?>
    fun getCardsByTag(tag: String): Flow<List<Card>>
    fun getCardsByType(proyectoId: String, tipo: CardType): Flow<List<Card>>
    fun getScenesForTimeline(proyectoId: String): Flow<List<Card>>
    fun searchCards(query: String): Flow<List<Card>>
    suspend fun upsertCard(card: Card)
    suspend fun deleteCard(id: String)
    suspend fun updateTags(cardId: String, tags: List<String>)
}
