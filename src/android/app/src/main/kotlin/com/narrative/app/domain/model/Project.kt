package com.narrative.app.domain.model

data class Project(
    val id: String,
    val nombre: String,
    val tipo: ProjectType = ProjectType.ONE_SHOT,
    val division: ProjectDivision = ProjectDivision.NINGUNA,
    val color: String = "#9B7FC7",
    val fechaCreacion: Long,
    val fechaEdicion: Long,
)

data class Container(
    val id: String,
    val nombre: String,
    val tipo: ContainerType,
    val proyectoId: String,
    val orden: Int,
    val descripcion: String? = null,
    val color: String? = null,
)

enum class ProjectType(val label: String) {
    ONE_SHOT("One-shot"),
    SERIE("Serie"),
    TEMPORADAS("Temporadas"),
    VIDEOJUEGO("Videojuego"),
    ANTOLOGIA("Antología"),
    OTRO("Otro"),
}

enum class ProjectDivision(val label: String) {
    NINGUNA("Ninguna"),
    ACTOS("Actos"),
    CAPITULOS("Capítulos"),
    EPISODIOS("Episodios"),
    TEMPORADAS("Temporadas"),
    NIVELES("Niveles"),
}

enum class ContainerType(val label: String) {
    ACTO("Acto"),
    CAPITULO("Capítulo"),
    EPISODIO("Episodio"),
    TEMPORADA("Temporada"),
    NIVEL("Nivel"),
    PARTE("Parte"),
}
