package com.narrative.app.ui.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.EscenaData
import com.narrative.app.domain.model.PersonajeData
import com.narrative.app.domain.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardDetailUiState(
    val card: Card? = null,
    val titulo: String = "",
    val cuerpo: String = "",
    val isLoading: Boolean = true,
)

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : ViewModel() {

    private val _cardId = MutableStateFlow<String?>(null)
    private val _titulo = MutableStateFlow("")
    private val _cuerpo = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CardDetailUiState> = _cardId
        .filterNotNull()
        .flatMapLatest { id -> cardRepository.getCard(id) }
        .combine(_titulo) { card, titulo -> card to titulo }
        .combine(_cuerpo) { (card, titulo), cuerpo ->
            CardDetailUiState(card = card, titulo = titulo, cuerpo = cuerpo, isLoading = card == null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CardDetailUiState())

    fun loadCard(id: String) {
        _cardId.value = id
        viewModelScope.launch {
            cardRepository.getCard(id).firstOrNull()?.let { card ->
                _titulo.value = card.titulo ?: ""
                _cuerpo.value = card.cuerpo
            }
        }
    }

    fun onTituloChange(value: String) { _titulo.value = value }
    fun onCuerpoChange(value: String) { _cuerpo.value = value }

    fun onPersonajeChange(data: PersonajeData) {
        val card = uiState.value.card ?: return
        viewModelScope.launch { cardRepository.upsertCard(card.copy(personaje = data)) }
    }

    fun onEscenaChange(data: EscenaData) {
        val card = uiState.value.card ?: return
        viewModelScope.launch { cardRepository.upsertCard(card.copy(escena = data)) }
    }

    fun save() {
        val card = uiState.value.card ?: return
        viewModelScope.launch {
            cardRepository.upsertCard(
                card.copy(
                    titulo = _titulo.value.trim().ifBlank { null },
                    cuerpo = _cuerpo.value,
                    fechaEdicion = System.currentTimeMillis(),
                )
            )
        }
    }
}
