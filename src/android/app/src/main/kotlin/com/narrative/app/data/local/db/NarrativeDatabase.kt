package com.narrative.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.narrative.app.data.local.dao.CardDao
import com.narrative.app.data.local.dao.ProjectDao
import com.narrative.app.data.local.entity.CardEntity
import com.narrative.app.data.local.entity.CardTagEntity
import com.narrative.app.data.local.entity.ContainerEntity
import com.narrative.app.data.local.entity.ProjectEntity

@Database(
    entities = [
        CardEntity::class,
        CardTagEntity::class,
        ProjectEntity::class,
        ContainerEntity::class,
    ],
    version = 1,
    exportSchema = true,  // exporta schema a /schemas para control de migraciones
)
abstract class NarrativeDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun projectDao(): ProjectDao

    companion object {
        const val DATABASE_NAME = "narrative.db"
    }
}
