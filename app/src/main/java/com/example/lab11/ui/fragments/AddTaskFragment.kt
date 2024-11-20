package com.example.lab11.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lab11.R
import com.example.lab11.data.models.Task
import com.example.lab11.databinding.FragmentAddTaskBinding
import com.example.lab11.ui.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTaskFragment : Fragment(R.layout.fragment_add_task) {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormat.format(Date(System.currentTimeMillis()))
        binding.taskDateInput.text = date

        binding.addTaskButton.setOnClickListener {
            val taskName = binding.taskNameInput.text.toString()
            val taskDescription = binding.taskDescriptionInput.text.toString()

            if (taskName.isNotEmpty() && taskDescription.isNotEmpty()) {
                val newTask = Task(
                    name = taskName,
                    description = taskDescription,
                    dateCreated = System.currentTimeMillis(),
                    isCompleted = false
                )

                taskViewModel.insert(newTask)

                Snackbar.make(view, "Task added", Snackbar.LENGTH_SHORT).show()

                findNavController().navigateUp()
            } else {
                Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
