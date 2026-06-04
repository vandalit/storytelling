package com.narrative.app.ui.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrative.app.domain.model.Card
import com.narrative.app.domain.model.Container
import com.narrative.app.domain.model.Project
import com.narrative.app.domain.repository.CardRepository
import com.narrative.app.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ProjectUiState(
    val project: Project? = null,
    val cards: List<Card> = emptyList(),
    val containers: List<Container> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val cardRepository: CardRepository,
) : ViewModel() {

    private val _projectId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProjectUiState> = _projectId
        .filterNotNull()
        .flatMapLatest { id ->
            combine(
                projectRepository.getProject(id),
                cardRepository.getProjectCards(id),
                projectRepository.getContainers(id),
            ) { project, cards, containers ->
                ProjectUiState(
                    project    = project,
                    cards      = cards,
                    containers = containers,
                    isLoading  = false,
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProjectUiState())

    fun loadProject(id: String) { _projectId.value = id }
}
