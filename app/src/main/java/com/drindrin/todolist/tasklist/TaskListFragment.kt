package com.drindrin.todolist.tasklist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.drindrin.todolist.R
import com.drindrin.todolist.databinding.FragmentTaskListBinding
import com.drindrin.todolist.models.Task
import com.drindrin.todolist.network.Api
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.drindrin.todolist.utils.NavigationUtils.Companion.getCurrentNavigationResultLiveData
import com.drindrin.todolist.utils.NavigationUtils.Companion.removeCurrentNavigationResult
import com.drindrin.todolist.utils.NavigationUtils.Companion.removePreviousNavigationResult
import com.drindrin.todolist.utils.NavigationUtils.Companion.setCurrentNavigationResult
import com.drindrin.todolist.utils.NavigationUtils.Companion.setPreviousNavigationResult


class TaskListFragment : Fragment() {
    private val taskListAdapter: TaskListAdapter = createAdapter()
    private val viewModel: TasksViewModel by viewModels()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadTasks()

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = taskListAdapter

        getCurrentNavigationResultLiveData<Task>("newTask")?.observe(viewLifecycleOwner) { task ->
            viewModel.editTask(task)
            removeCurrentNavigationResult<Task>("newTask")
        }

        binding.addTaskButton.setOnClickListener {
            removeCurrentNavigationResult<Task>("task")
            findNavController().navigate(R.id.action_taskListFragment_to_formFragment)
        }

        binding.userPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_userInfoFragment)
        }

        lifecycleScope.launch {
            viewModel.taskList.collectLatest { newList ->
                taskListAdapter.submitList(newList)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.userInfoTextView.text = "Bienvenue ${userInfo.firstName} ${userInfo.lastName}"
            binding.userPhoto.load(userInfo.avatar) {
                error(R.drawable.ic_launcher_background)
                transformations(CircleCropTransformation())
            }
        }

    }

    private fun createAdapter(): TaskListAdapter {
        val adapterListener = object : TaskListListener {
            override fun onClickDelete(task: Task) {
                viewModel.deleteTask(task)
            }

            override fun onClickEdit(task: Task) {
                setCurrentNavigationResult(task, "task")
                findNavController().navigate(R.id.action_taskListFragment_to_formFragment)
            }

        }
        return TaskListAdapter(adapterListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
