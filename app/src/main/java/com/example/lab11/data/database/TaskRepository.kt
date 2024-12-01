package com.example.lab11.data.database

import androidx.lifecycle.LiveData
import com.example.lab11.data.models.Task

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun getAllTasks(): List<Task> = taskDao.getAllTasks()

    suspend fun getTaskById(taskId: Int): Task? = taskDao.getTaskById(taskId)

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }
}