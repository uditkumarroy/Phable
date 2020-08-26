package com.basicmvi.ui.state

import com.phable.models.Task
import com.phable.util.DataState

data class MainViewState(
    var taskList: List<Task>? = null,
    var task: Task? = null
)