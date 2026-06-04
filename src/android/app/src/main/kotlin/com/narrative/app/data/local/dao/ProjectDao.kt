package com.narrative.app.data.local.dao

import androidx.room.*
import com.narrative.app.data.local.entity.ContainerEntity
import com.narrative.app.data.local.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects ORDER BY fecha_edicion DESC")
    fun getProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProject(id: String): Flow<ProjectEntity?>

    @Query("SELECT * FROM containers WHERE proyecto_id = :proyectoId ORDER BY orden ASC")
    fun getContainers(proyectoId: String): Flow<List<ContainerEntity>>

    @Upsert
    suspend fun upsertProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String)

    @Upsert
    suspend fun upsertContainer(container: ContainerEntity)
}
