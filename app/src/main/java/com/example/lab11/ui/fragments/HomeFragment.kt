package com.example.lab11.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab11.R
import com.example.lab11.data.models.Task
import com.example.lab11.databinding.FragmentHomeBinding
import com.example.lab11.ui.activities.MainActivity
import com.example.lab11.ui.adapters.TaskAdapter
import com.example.lab11.ui.viewmodels.TaskViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }

        val recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        taskAdapter = TaskAdapter(emptyList(), { task ->
            updateTask(task)
        }) { task ->
            val action = HomeFragmentDirections.actionHomeFragmentToTaskDetailsFragment(task.id)
            findNavController().navigate(action)
        }
        recyclerView.adapter = taskAdapter

        taskViewModel.filteredTasks.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isEmpty()) {
                binding.tvEmptyList.visibility = View.VISIBLE
            } else {
                binding.tvEmptyList.visibility = View.GONE
            }
            taskAdapter.updateTasks(tasks)
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showToolbarIcons(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showToolbarIcons(false)
    }
    private fun updateTask(task: Task) {
        taskViewModel.update(task)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
