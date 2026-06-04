package com.narrative.app.di

import android.content.Context
import androidx.room.Room
import com.narrative.app.data.local.db.NarrativeDatabase
import com.narrative.app.data.local.dao.CardDao
import com.narrative.app.data.local.dao.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NarrativeDatabase =
        Room.databaseBuilder(context, NarrativeDatabase::class.java, NarrativeDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()  // cambiar a migraciones explícitas en producción
            .build()

    @Provides fun provideCardDao(db: NarrativeDatabase): CardDao = db.cardDao()
    @Provides fun provideProjectDao(db: NarrativeDatabase): ProjectDao = db.projectDao()
}
