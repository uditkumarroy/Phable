package com.basicmvi.ui.state

import com.phable.models.Task

sealed class MainStateEvent {
    class GetTaskListEvents: MainStateEvent()
    class GetTaskEvent(val id:Int): MainStateEvent()
    class CreateTaskEvent(val task: Task): MainStateEvent()
    class None: MainStateEvent()
}