package com.phable.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basicmvi.ui.state.MainStateEvent
import com.basicmvi.ui.state.MainStateEvent.*
import com.phable.models.Task
import com.phable.repository.TaskRepository
import com.phable.util.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainVewModel @ViewModelInject constructor(val taskRepository: TaskRepository):ViewModel(){

    private val _dataState: MutableLiveData<DataState<List<Task>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Task>>>
        get() = _dataState

    private val _dataTaskState: MutableLiveData<DataState<Long>> = MutableLiveData()
    val dataTaskState: LiveData<DataState<Long>>
        get() = _dataTaskState

    private val _dataGetTaskState: MutableLiveData<DataState<Task>> = MutableLiveData()
    val dataGetTaskState: LiveData<DataState<Task>>
        get() = _dataGetTaskState

    private val _dataDeleteTaskState: MutableLiveData<DataState<String>> = MutableLiveData()
    val dataDeleteTaskState: LiveData<DataState<String>>
        get() = _dataDeleteTaskState

    private val _dataUpdateTaskState: MutableLiveData<DataState<Int>> = MutableLiveData()
    val dataUpdateTaskState: LiveData<DataState<Int>>
        get() = _dataUpdateTaskState

    fun taskStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is GetTaskListEvents -> {
                    taskRepository.getTaskList()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }.launchIn(viewModelScope)
                }

                is CreateTaskEvent ->{
                    taskRepository.insert(mainStateEvent.task)
                        .onEach { taskId->
                            _dataTaskState.value = taskId
                        }.launchIn(viewModelScope)
                }

                is GetTaskEvent -> {
                    taskRepository.getTask(mainStateEvent.id)
                        .onEach {task->
                            _dataGetTaskState.value = task
                        }.launchIn(viewModelScope)
                }

                is DeleteTaskEvent ->{
                    taskRepository.delete(mainStateEvent.task)
                        .onEach {task->
                            _dataDeleteTaskState.value = task
                        }.launchIn(viewModelScope)
                }

                is UpdateTaskEvent ->{
                    taskRepository.update(mainStateEvent.task)
                        .onEach {task->
                            _dataUpdateTaskState.value = task
                        }.launchIn(viewModelScope)
                }

                is None -> {

                }
            }
        }
    }




}