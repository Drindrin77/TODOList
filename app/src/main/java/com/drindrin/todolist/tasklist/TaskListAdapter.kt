package com.drindrin.todolist.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drindrin.todolist.databinding.ItemTaskBinding
import com.drindrin.todolist.models.Task

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}
object TaskListDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.title == newItem.title && oldItem.description == newItem.description
    }
}

class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskListDiffCallback) {

    private lateinit var binding: ItemTaskBinding

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder() : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.taskTitle.text = task.displayTask()
            binding.deleteTaskButton.setOnClickListener{
                listener.onClickDelete(task)
            }
            binding.editTaskButton.setOnClickListener{
                listener.onClickEdit(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position));
    }

    // Déclaration de la variable lambda dans l'adapter:
    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}
}