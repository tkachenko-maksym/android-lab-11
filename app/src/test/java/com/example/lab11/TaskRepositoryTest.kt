package com.example.lab11

import com.example.lab11.data.database.TaskDao
import com.example.lab11.data.database.TaskRepository
import com.example.lab11.data.models.Task
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TaskRepositoryTest {

    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        taskDao = mockk()
        repository = TaskRepository(taskDao)
    }

    @Test
    fun getAllTasks_returnsListOfTasks() = runTest {
        // Given
        val tasks = listOf(
            Task(1, "Task 1", "Description 1", System.currentTimeMillis()),
            Task(2, "Task 2", "Description 2", System.currentTimeMillis())
        )
        coEvery { taskDao.getAllTasks() } returns tasks

        // When
        val result = repository.getAllTasks()

        // Then
        assertEquals(tasks, result)
        coVerify(exactly = 1) { taskDao.getAllTasks() }
    }

    @Test
    fun getTaskById_returnsTask() = runTest {
        // Given
        val task = Task(1, "Task 1", "Description 1", System.currentTimeMillis())
        coEvery { taskDao.getTaskById(1) } returns task

        // When
        val result = repository.getTaskById(1)

        // Then
        assertEquals(task, result)
        coVerify(exactly = 1) { taskDao.getTaskById(1) }
    }

    @Test
    fun insert_callsDaoInsert() = runTest {
        // Given
        val task = Task(1, "Task 1", "Description 1", System.currentTimeMillis())
        coEvery { taskDao.insert(task) } just runs

        // When
        repository.insert(task)

        // Then
        coVerify(exactly = 1) { taskDao.insert(task) }
    }
}


