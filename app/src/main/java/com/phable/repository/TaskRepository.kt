package com.phable.repository

import com.phable.models.Task
import com.phable.util.DataState
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun  getTaskList(): Flow<DataState<List<Task>>>

    fun getTask(id:Int): Flow<DataState<Task>>

    fun update(task: Task): Flow<DataState<Int>>

    fun insert(task: Task): Flow<DataState<Long>>
}