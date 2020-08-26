package com.phable.data.roompers

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class PhableDataBase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        val DATA_BASE_NAME: String = "Phable_DB"
    }
}