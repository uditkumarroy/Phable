package com.phable.repository

import com.phable.data.roompers.TaskDao
import com.phable.models.Task
import com.phable.repository.mappers.TaskMapper
import com.phable.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class TaskRepositoryImpl constructor(val taskDao: TaskDao, val taskMapper: TaskMapper){

    suspend fun getTaskList(): Flow<DataState<List<Task>>> = flow {
        emit(DataState.Loading)

        try {
            val taskList = taskDao.getTaskList()
            emit(DataState.Sucess(taskMapper.mapFromEntityList(taskList)))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }

    }

    suspend fun getTask(id:Int): Flow<DataState<Task>> = flow {
        emit(DataState.Loading)
        try {
            val task = taskDao.getTask(id)
            emit(DataState.Sucess(taskMapper.mapFromEntity(task)))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }

    suspend fun update(task: Task): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        try {
            val count = taskDao.updateTask(taskMapper.mapToEntity(task))
            emit(DataState.Sucess(count))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }

    suspend fun insert(task: Task): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)
        try {
            val task = taskDao.insert(taskMapper.mapToEntity(task))
            emit(DataState.Sucess(task))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }


}