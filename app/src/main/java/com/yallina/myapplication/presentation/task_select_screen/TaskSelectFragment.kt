package com.yallina.myapplication.presentation.task_select_screen

import android.os.Bundle
import android.view.*
import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.yallina.myapplication.MyApplication
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.GetTasksOnDayUseCase
import com.yallina.myapplication.presentation.task_select_screen.model.TaskPresentationModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import javax.inject.Inject

class TaskSelectFragment : Fragment() {
    @Inject
    lateinit var getTasksOnDay: GetTasksOnDayUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.get().injector.inject(this)
    }

    private val taskSelectViewModel by activityViewModels<TaskSelectViewModel>()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireActivity()).apply {
            setContent {
                TaskSelectScreenComposable(taskModelArray = taskSelectViewModel.initPresentationArray())
            }
        }
    }
}

@Composable
fun TaskSelectScreenComposable(
    modifier: Modifier = Modifier,
    taskModelArray: Array<TaskPresentationModel>
) {
    val scrollState = rememberScrollState()
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.Add, "")
        }
    }) {paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(text = "Choose date")
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AndroidView(
                    { CalendarView(it) },
                    modifier = Modifier
                        .wrapContentWidth(),
                    update = { view ->

                    }
                )
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                taskModelArray.map { taskModel ->
                    HourOfTheDayItemComposable(taskModel = taskModel)
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun HourOfTheDayItemComposable(
    modifier: Modifier = Modifier,
    taskModel: TaskPresentationModel
){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp)){
            Text(text = taskModel.durationAsString())

            Column() {
                taskModel.taskList.mapIndexed {index, task ->
                    ClickableText(text = AnnotatedString("${index+1}. ${task.name}"), onClick = {}, overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(){
    TaskSelectScreenComposable(taskModelArray = tasksArray)
}

val tasksArray = arrayOf(
    TaskPresentationModel(
        timeStart = LocalTime.of(0,0),
        timeEnd = LocalTime.of(1,0),
        taskList = listOf(Task(name="First", dateEnd = LocalDateTime.now(), dateStart = LocalDateTime.now(), description = "wewewewewewew", id=1))
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(1,0),
        timeEnd = LocalTime.of(2,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(2,0),
        timeEnd = LocalTime.of(3,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(3,0),
        timeEnd = LocalTime.of(4,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(4,0),
        timeEnd = LocalTime.of(5,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(5,0),
        timeEnd = LocalTime.of(6,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(6,0),
        timeEnd = LocalTime.of(7,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(7,0),
        timeEnd = LocalTime.of(8,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(8,0),
        timeEnd = LocalTime.of(9,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(9,0),
        timeEnd = LocalTime.of(10,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(10,0),
        timeEnd = LocalTime.of(11,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(11,0),
        timeEnd = LocalTime.of(12,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(12,0),
        timeEnd = LocalTime.of(13,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(13,0),
        timeEnd = LocalTime.of(14,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(14,0),
        timeEnd = LocalTime.of(15,0),
    ),TaskPresentationModel(
        timeStart = LocalTime.of(15,0),
        timeEnd = LocalTime.of(16,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(16,0),
        timeEnd = LocalTime.of(17,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(17,0),
        timeEnd = LocalTime.of(18,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(18,0),
        timeEnd = LocalTime.of(19,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(19,0),
        timeEnd = LocalTime.of(20,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(20,0),
        timeEnd = LocalTime.of(21,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(21,0),
        timeEnd = LocalTime.of(22,0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(22,0),
        timeEnd = LocalTime.of(23,0),
    ),TaskPresentationModel(
        timeStart = LocalTime.of(23,0),
        timeEnd = LocalTime.of(23,59),
    )
)