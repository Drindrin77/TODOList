package com.drindrin.todolist.form

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.drindrin.todolist.databinding.FragmentFormBinding
import com.drindrin.todolist.models.Task
import com.drindrin.todolist.utils.NavigationUtils.Companion.getCurrentNavigationResult
import com.drindrin.todolist.utils.NavigationUtils.Companion.getPreviousNavigationResult
import com.drindrin.todolist.utils.NavigationUtils.Companion.removeCurrentNavigationResult
import com.drindrin.todolist.utils.NavigationUtils.Companion.removePreviousNavigationResult
import com.drindrin.todolist.utils.NavigationUtils.Companion.setPreviousNavigationResult
import java.util.*

class FormFragment : Fragment() {
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var task = getPreviousNavigationResult<Task>("task")

        binding.titleText.setText(task?.title)
        binding.descriptionText.setText(task?.description)

        binding.validateButton.setOnClickListener {

            val newTask = Task(
                id = task?.id ?: UUID.randomUUID().toString(),
                title = binding.titleText.text.toString(),
                description = binding.descriptionText.text.toString()
            )

            setPreviousNavigationResult(newTask, "newTask")
            findNavController().popBackStack()
        }
    }
}