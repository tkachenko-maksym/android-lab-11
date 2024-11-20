package com.example.lab11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.lab11.data.database.TaskDatabase
import com.example.lab11.data.database.TaskRepository
import com.example.lab11.data.models.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    private val allTasks: LiveData<List<Task>>

    private val searchQuery = MutableLiveData("")
    //true for ascending, false for descending
    private val sortType = MutableLiveData(true)

    init {
        val taskDao = TaskDatabase.getInstance(application)!!.taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasksLiveData
    }

    val filteredTasks: LiveData<List<Task>> = allTasks.switchMap { tasks ->
        searchQuery.switchMap { query ->
            sortType.map { isAscending ->
                filterAndSortTasks(tasks, query, isAscending)
            }
        }
    }

    private fun filterAndSortTasks(
        tasks: List<Task>, query: String, isAscending: Boolean
    ): List<Task> {
        // Filter tasks based on the search query
        val filteredTasks = tasks.filter { task ->
            task.name.contains(query, ignoreCase = true) || task.description.contains(
                query, ignoreCase = true
            )
        }

        // Sort tasks based on the sort order
        return if (isAscending) {
            filteredTasks.sortedBy { it.dateCreated }
        } else {
            filteredTasks.sortedByDescending { it.dateCreated }
        }
    }


    fun toggleSortOrder() {
        sortType.value = !(sortType.value ?: true)
    }
    fun getSortOrder(): Boolean {
        return sortType.value ?: true
    }
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun getTaskById(taskId: Int): LiveData<Task?> {
        return repository.getTaskById(taskId)
    }

    fun insert(task: Task) {
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}
