package com.yallina.myapplication.presentation.task_info_screen

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yallina.myapplication.R
import kotlin.properties.Delegates

/**
 * [Fragment] that displays task information
 * @property taskId task identifier passed in [getArguments]
 */
class TaskInfoFragment : Fragment(R.layout.task_info_fragment) {
    private var taskId by Delegates.notNull<Int>()

    private val viewModel by viewModels<TaskInfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            taskId = it.getInt(TASK_ID_KEY)
            viewModel.showTaskById(taskId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView = view.findViewById<TextView>(R.id.taskName)
        val descTextView = view.findViewById<TextView>(R.id.taskDescription)
        val dateStartTextView = view.findViewById<TextView>(R.id.taskDateStart)
        val dateEndTextView = view.findViewById<TextView>(R.id.taskDateEnd)

        requireActivity().actionBar?.setTitle(R.string.task_info_actionbar_title)

        viewModel.task.observe(viewLifecycleOwner) { task ->
            nameTextView.text = task.name
            descTextView.text = task.description
            dateStartTextView.text = task.dateStart
            dateEndTextView.text = task.dateEnd
        }
    }

    companion object {
        private const val TASK_ID_KEY = "taskId"
    }
}