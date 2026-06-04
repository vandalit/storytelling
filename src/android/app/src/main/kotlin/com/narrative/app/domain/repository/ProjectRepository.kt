package com.narrative.app.domain.repository

import com.narrative.app.domain.model.Container
import com.narrative.app.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getProjects(): Flow<List<Project>>
    fun getProject(id: String): Flow<Project?>
    fun getContainers(proyectoId: String): Flow<List<Container>>
    suspend fun upsertProject(project: Project)
    suspend fun deleteProject(id: String)
    suspend fun upsertContainer(container: Container)
}
