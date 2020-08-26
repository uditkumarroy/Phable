package com.phable.di

import android.content.Context
import androidx.room.Room
import com.phable.data.roompers.PhableDataBase
import com.phable.data.roompers.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDataBase(@ApplicationContext context: Context):PhableDataBase{
        return Room.databaseBuilder(context, PhableDataBase::class.java, PhableDataBase.DATA_BASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(phableDataBase: PhableDataBase):TaskDao{
        return phableDataBase.taskDao()
    }
}