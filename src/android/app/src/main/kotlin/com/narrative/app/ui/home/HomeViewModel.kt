package com.narrative.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.Project
import com.narrative.app.domain.repository.CardRepository
import com.narrative.app.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val projects: List<Project> = emptyList(),
    val inboxCards: List<Card> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    projectRepository: ProjectRepository,
    cardRepository: CardRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        projectRepository.getProjects(),
        cardRepository.getInboxCards(),
    ) { projects, inboxCards ->
        HomeUiState(
            projects   = projects,
            inboxCards = inboxCards,
            isLoading  = false,
        )
    }.stateIn(
        scope         = viewModelScope,
        started       = SharingStarted.WhileSubscribed(5_000),
        initialValue  = HomeUiState(),
    )
}
