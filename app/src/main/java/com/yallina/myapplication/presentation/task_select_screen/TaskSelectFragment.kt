package com.yallina.myapplication.presentation.task_select_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yallina.myapplication.MyApplication
import com.yallina.myapplication.presentation.task_select_screen.compose.TaskSelectScreenComposable
import com.yallina.myapplication.presentation.task_select_screen.model.LocalDatePresentationModel
import org.threeten.bp.LocalDate
import javax.inject.Inject

class TaskSelectFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: TaskSelectViewModelFactory

    private val taskSelectViewModel by activityViewModels<TaskSelectViewModel>() {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.get().injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val taskPresentationArrayState =
                    taskSelectViewModel.taskPresentationArray.observeAsState(
                        emptyArray()
                    )

                val chosenDayState = taskSelectViewModel.chosenDate.observeAsState(
                    LocalDatePresentationModel(LocalDate.now())
                )

                TaskSelectScreenComposable(
                    taskModelArray = taskPresentationArrayState.value,
                    chosenDay = chosenDayState.value,
                    onCalendarPick = { date -> taskSelectViewModel.onDayChosen(date) },
                    onTaskClick = { taskId -> navigateToTaskInfoFragment(taskId) },
                    onNewTaskClick = { navigateToNewTaskFragment() }
                )


            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskSelectViewModel.snackbar.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
                taskSelectViewModel.onSnackbarShow()
            }
        }
    }

    private fun navigateToTaskInfoFragment(id: Int) {
        val action = TaskSelectFragmentDirections.actionTaskSelectFragmentToTaskInfoFragment(id)
        findNavController().navigate(action)
    }

    private fun navigateToNewTaskFragment() {
        val action = TaskSelectFragmentDirections.actionTaskSelectFragmentToNewTaskFragment()
        findNavController().navigate(action)
    }
}