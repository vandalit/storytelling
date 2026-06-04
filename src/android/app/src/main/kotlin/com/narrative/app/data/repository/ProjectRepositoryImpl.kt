package com.narrative.app.data.repository

import com.narrative.app.data.local.dao.ProjectDao
import com.narrative.app.data.local.entity.toEntity
import com.narrative.app.domain.model.Container
import com.narrative.app.domain.model.Project
import com.narrative.app.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val dao: ProjectDao,
) : ProjectRepository {

    override fun getProjects(): Flow<List<Project>> =
        dao.getProjects().map { it.map { entity -> entity.toDomain() } }

    override fun getProject(id: String): Flow<Project?> =
        dao.getProject(id).map { it?.toDomain() }

    override fun getContainers(proyectoId: String): Flow<List<Container>> =
        dao.getContainers(proyectoId).map { it.map { entity -> entity.toDomain() } }

    override suspend fun upsertProject(project: Project) =
        dao.upsertProject(project.toEntity())

    override suspend fun deleteProject(id: String) =
        dao.deleteProject(id)

    override suspend fun upsertContainer(container: Container) =
        dao.upsertContainer(container.toEntity())
}
