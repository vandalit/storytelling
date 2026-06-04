package com.narrative.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.narrative.app.domain.model.Container
import com.narrative.app.domain.model.ContainerType
import com.narrative.app.domain.model.Project
import com.narrative.app.domain.model.ProjectDivision
import com.narrative.app.domain.model.ProjectType

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val tipo: String,
    val division: String,
    val color: String,
    val fecha_creacion: Long,
    val fecha_edicion: Long,
) {
    fun toDomain() = Project(
        id            = id,
        nombre        = nombre,
        tipo          = ProjectType.valueOf(tipo),
        division      = ProjectDivision.valueOf(division),
        color         = color,
        fechaCreacion = fecha_creacion,
        fechaEdicion  = fecha_edicion,
    )
}

fun Project.toEntity() = ProjectEntity(
    id            = id,
    nombre        = nombre,
    tipo          = tipo.name,
    division      = division.name,
    color         = color,
    fecha_creacion = fechaCreacion,
    fecha_edicion  = fechaEdicion,
)

@Entity(
    tableName = "containers",
    indices = [androidx.room.Index("proyecto_id")]
)
data class ContainerEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val tipo: String,
    val proyecto_id: String,
    val orden: Int,
    val descripcion: String?,
    val color: String?,
) {
    fun toDomain() = Container(
        id          = id,
        nombre      = nombre,
        tipo        = ContainerType.valueOf(tipo),
        proyectoId  = proyecto_id,
        orden       = orden,
        descripcion = descripcion,
        color       = color,
    )
}

fun Container.toEntity() = ContainerEntity(
    id          = id,
    nombre      = nombre,
    tipo        = tipo.name,
    proyecto_id = proyectoId,
    orden       = orden,
    descripcion = descripcion,
    color       = color,
)
