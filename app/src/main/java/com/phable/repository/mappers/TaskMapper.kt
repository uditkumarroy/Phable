package com.phable.repository.mappers

import com.phable.data.roompers.TaskEntity
import com.phable.models.Task
import com.phable.util.EntityMapping
import javax.inject.Inject


class TaskMapper @Inject constructor() :EntityMapping<TaskEntity,Task>{
    override fun mapFromEntity(entity: TaskEntity): Task {
        return Task(
            id = entity.id,
            name = entity.name,
            email = entity.email
        )
    }

    override fun mapToEntity(domainModel: Task): TaskEntity {
        return TaskEntity(
            id = domainModel.id,
            name = domainModel.name,
            email = domainModel.email
        )
    }

    fun mapFromEntityList(entities: List<TaskEntity>): List<Task> {
        return entities.map { mapFromEntity(it) }
    }

}