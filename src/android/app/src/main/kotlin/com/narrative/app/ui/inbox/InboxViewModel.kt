package com.narrative.app.ui.inbox

import androidx.lifecycle.ViewModel
import com.narrative.app.domain.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    cardRepository: CardRepository,
) : ViewModel() {
    val inboxCards = cardRepository.getInboxCards()
}
