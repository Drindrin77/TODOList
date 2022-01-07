package com.drindrin.todolist.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drindrin.todolist.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch {
            val tasks = repository.loadTasks()
            if (tasks != null) _taskList.value = tasks
        }

    }
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            if(repository.removeTask(task) == true) {
                _taskList.value = _taskList.value - task
            }
        }
    }
    private fun addTask(task: Task) {
        viewModelScope.launch {
            val createdTask = repository.createTask(task)
            if (createdTask != null) {
                _taskList.value = _taskList.value + createdTask
            }
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch {
            val existingTask = firstOrNullTask(task)
            if (existingTask != null){
                val updatedTask = repository.updateTask(task)
                if(updatedTask != null){
                    _taskList.value = _taskList.value - existingTask + updatedTask
                }
            }else{
                addTask(task)
            }

        }
    }

    private fun firstOrNullTask(task: Task): Task?{
        return taskList.value.firstOrNull { it.id == task.id }
    }
}