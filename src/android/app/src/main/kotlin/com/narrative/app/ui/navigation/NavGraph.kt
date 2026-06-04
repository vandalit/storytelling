package com.narrative.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.narrative.app.ui.card.CardDetailScreen
import com.narrative.app.ui.home.HomeScreen
import com.narrative.app.ui.inbox.InboxScreen
import com.narrative.app.ui.project.ProjectScreen

@Composable
fun NarrativeNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Home) {

        composable<Home> {
            HomeScreen(
                onProjectClick  = { id -> navController.navigate(ProjectDetail(id)) },
                onInboxClick    = { navController.navigate(Inbox) },
                onCardClick     = { id -> navController.navigate(CardDetail(id)) },
            )
        }

        composable<Inbox> {
            InboxScreen(
                onBack      = { navController.popBackStack() },
                onCardClick = { id -> navController.navigate(CardDetail(id)) },
            )
        }

        composable<ProjectDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<ProjectDetail>()
            ProjectScreen(
                projectId   = route.projectId,
                onBack      = { navController.popBackStack() },
                onCardClick = { id -> navController.navigate(CardDetail(id)) },
            )
        }

        composable<CardDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<CardDetail>()
            CardDetailScreen(
                cardId = route.cardId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
