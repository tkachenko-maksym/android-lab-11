package com.example.lab11.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lab11.data.models.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks ORDER BY dateCreated DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task?>

//    @Query("SELECT * FROM tasks WHERE name LIKE :searchQuery OR description LIKE :searchQuery ORDER BY dateCreated DESC")
//    fun getTaskByNameOrDesc(searchQuery: String): List<Task>
}
