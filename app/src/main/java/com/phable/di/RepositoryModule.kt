package com.phable.di

import com.phable.data.roompers.TaskDao
import com.phable.data.roompers.TaskEntity
import com.phable.models.Task
import com.phable.repository.TaskRepositoryImpl
import com.phable.repository.mappers.TaskMapper
import com.phable.util.EntityMapping
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideTaskMapper():EntityMapping<TaskEntity,Task>{
        return TaskMapper()
    }

    @Singleton
    @Provides
    fun provideTaskRepository(taskDao: TaskDao,taskMapper: TaskMapper):TaskRepositoryImpl{
        return TaskRepositoryImpl(taskDao,taskMapper)
    }
}