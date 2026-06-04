package com.narrative.app.ui.navigation

import kotlinx.serialization.Serializable

// Type-safe navigation routes (Navigation Compose 2.8+)

@Serializable object Home
@Serializable object Inbox
@Serializable data class ProjectDetail(val projectId: String)
@Serializable data class CardDetail(val cardId: String)
