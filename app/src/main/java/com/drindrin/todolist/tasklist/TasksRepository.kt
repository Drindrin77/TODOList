package com.drindrin.todolist.tasklist

import com.drindrin.todolist.models.Task
import com.drindrin.todolist.network.Api

class TasksRepository {

    private val webService = Api.tasksWebService

    suspend fun loadTasks(): List<Task>? {
        val response = webService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun removeTask(task: Task): Boolean? {
        val response = webService.delete(task.id)
        return response.isSuccessful
    }
    suspend fun createTask(task: Task) : Task?{
        val response = webService.create(task)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun updateTask(task: Task): Task? {
        val response = webService.update(task)
        return if (response.isSuccessful) response.body() else null
    }
}
