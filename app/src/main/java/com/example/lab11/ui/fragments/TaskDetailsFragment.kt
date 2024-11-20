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
import com.example.lab11.databinding.FragmentTaskDetailsBinding
import com.example.lab11.ui.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar


class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Int? = null
    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
        taskId = arguments?.let {
            TaskDetailsFragmentArgs.fromBundle(it).taskId
        }
        taskViewModel.getTaskById(taskId!!).observe(viewLifecycleOwner) { task ->
            task?.let {
                this.task = it
                updateUI()
            }
        }
        binding.editTaskButton.setOnClickListener {
            enableEditing()
        }
        binding.saveTaskButton.setOnClickListener {
            val updatedName = binding.taskNameDetailInput.text.toString()
            val updatedDescription = binding.taskDescriptionDetailInput.text.toString()
            val updatedStatus = binding.taskStatusDetailInputCheckBox.isChecked
            if (updatedName.isNotEmpty() && updatedDescription.isNotEmpty()) {
                val updatedTask = task.copy(
                    name = updatedName,
                    description = updatedDescription,
                    isCompleted = updatedStatus
                )
                taskViewModel.update(updatedTask)
                disableEditing()
                Snackbar.make(view, "Task updated", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.deleteTaskButton.setOnClickListener {
            taskViewModel.delete(task)
            Snackbar.make(view, "Task deleted", Snackbar.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun updateUI() {
        binding.taskNameDetail.text = task.name
        binding.taskDescriptionDetail.text = task.description
        binding.taskDateDetail.text = "Created: ${task.prettyDateFormat()}"
        binding.taskStatusDetail.text =
            if (task.isCompleted) "Status: Completed" else "Status: Not Completed"
    }

    private fun enableEditing() {
        // Hide TextViews, show EditTexts
        binding.taskNameDetail.visibility = View.GONE
        binding.taskDescriptionDetail.visibility = View.GONE
        binding.taskStatusDetail.visibility = View.GONE
        binding.taskDateDetail.visibility = View.GONE
        binding.editTaskButton.visibility = View.GONE

        binding.taskStatusDetailInput.visibility = View.VISIBLE
        binding.taskNameDetailInput.visibility = View.VISIBLE
        binding.taskDescriptionDetailInput.visibility = View.VISIBLE
        binding.saveTaskButton.visibility = View.VISIBLE

        // Populate EditTexts with current task data
        binding.taskNameDetailInput.setText(task.name)
        binding.taskDescriptionDetailInput.setText(task.description)
        binding.taskStatusDetailInputCheckBox.isChecked = task.isCompleted
    }

    private fun disableEditing() {
        // Hide EditTexts, show TextViews
        binding.taskNameDetailInput.visibility = View.GONE
        binding.taskDescriptionDetailInput.visibility = View.GONE
        binding.saveTaskButton.visibility = View.GONE
        binding.taskStatusDetailInput.visibility = View.GONE

        binding.editTaskButton.visibility = View.VISIBLE
        binding.taskNameDetail.visibility = View.VISIBLE
        binding.taskDescriptionDetail.visibility = View.VISIBLE
        binding.taskStatusDetail.visibility = View.VISIBLE
        binding.taskDateDetail.visibility = View.VISIBLE

        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}