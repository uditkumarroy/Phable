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
                        .onEach { task->
                            _dataTaskState.value = task
                        }.launchIn(viewModelScope)
                }

                is GetTaskEvent -> {
                    taskRepository.getTask(mainStateEvent.id)
                }

                is None -> {

                }
            }
        }
    }




}