package com.phable.data.roompers

import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskEntity: TaskEntity): Long

    @Query("SELECT * from task")
    suspend fun getTaskList(): List<TaskEntity>

    @Query("SELECT * from task where id = :id_Task")
    suspend fun getTask(id_Task: Int): TaskEntity

    @Update
    suspend fun updateTask(task: TaskEntity):Int

}