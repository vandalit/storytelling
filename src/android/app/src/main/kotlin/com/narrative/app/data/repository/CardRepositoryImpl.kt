package com.narrative.app.data.repository

import com.narrative.app.data.local.dao.CardDao
import com.narrative.app.data.local.entity.CardTagEntity
import com.narrative.app.data.local.entity.toEntity
import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.CardType
import com.narrative.app.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val dao: CardDao,
) : CardRepository {

    override fun getProjectCards(proyectoId: String): Flow<List<Card>> =
        dao.getProjectCards(proyectoId).map { entities ->
            entities.map { entity ->
                entity.toDomain(dao.getTagsForCard(entity.id))
            }
        }

    override fun getInboxCards(): Flow<List<Card>> =
        dao.getInboxCards().map { entities ->
            entities.map { entity ->
                entity.toDomain(dao.getTagsForCard(entity.id))
            }
        }

    override fun getCard(id: String): Flow<Card?> =
        dao.getCard(id).map { entity ->
            entity?.toDomain(dao.getTagsForCard(entity.id))
        }

    override fun getCardsByTag(tag: String): Flow<List<Card>> =
        dao.getCardsByTag(tag).map { entities ->
            entities.map { entity ->
                entity.toDomain(dao.getTagsForCard(entity.id))
            }
        }

    override fun getCardsByType(proyectoId: String, tipo: CardType): Flow<List<Card>> =
        dao.getCardsByType(proyectoId, tipo.name).map { entities ->
            entities.map { entity ->
                entity.toDomain(dao.getTagsForCard(entity.id))
            }
        }

    override fun getScenesForTimeline(proyectoId: String): Flow<List<Card>> =
        dao.getScenesForTimeline(proyectoId).map { entities ->
            entities.map { entity ->
                entity.toDomain(dao.getTagsForCard(entity.id))
            }
        }

    override fun searchCards(query: String): Flow<List<Card>> =
        dao.searchCards(query).map { entities ->
            entities.map { entity ->
                entity.toDomain(dao.getTagsForCard(entity.id))
            }
        }

    override suspend fun upsertCard(card: Card) {
        dao.upsertCard(card.toEntity())
        dao.updateTags(card.id, card.tags)
    }

    override suspend fun deleteCard(id: String) {
        dao.deleteCard(id)  // CardTagEntity se elimina en CASCADE
    }

    override suspend fun updateTags(cardId: String, tags: List<String>) {
        dao.updateTags(cardId, tags)
    }
}
