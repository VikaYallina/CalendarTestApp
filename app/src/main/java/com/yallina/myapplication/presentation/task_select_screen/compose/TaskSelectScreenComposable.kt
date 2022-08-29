package com.yallina.myapplication.presentation.task_select_screen.compose

import android.widget.CalendarView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yallina.myapplication.R
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.presentation.task_select_screen.model.LocalDatePresentationModel
import com.yallina.myapplication.presentation.task_select_screen.model.TaskPresentationModel
import com.yallina.myapplication.utils.MyDateTimeFormatter
import org.threeten.bp.*

@Composable
fun TaskSelectScreenComposable(
    modifier: Modifier = Modifier,
    taskModelArray: Array<TaskPresentationModel>,
    onCalendarPick: (LocalDate) -> Unit,
    onTaskClick: (Int) -> Unit,
    chosenDay: LocalDatePresentationModel,
    onNewTaskClick: () -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { onNewTaskClick() }) {
            Icon(Icons.Filled.Add, "")
        }
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TaskSelectCalendarComposable(
                chosenDay = chosenDay,
                onCalendarPick = onCalendarPick
            )

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.task_selection_screen_chosen_date))
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.purple_700)
                        )
                    ) {
                        append(chosenDay.toFormattedString(MyDateTimeFormatter.isoLocalDateFormatter))
                    }
                })

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(items = taskModelArray, itemContent = { taskModel ->
                    HourOfTheDayItemComposable(taskModel = taskModel, onTaskClick = onTaskClick)
                })

                item { Spacer(modifier = Modifier.height(70.dp)) }
            }
        }
    }
}

@Composable
fun TaskSelectCalendarComposable(
    chosenDay: LocalDatePresentationModel,
    onCalendarPick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            { CalendarView(it) },
            modifier = Modifier
                .wrapContentWidth(),
            update = { view ->
                view.date = chosenDay.toMillis()
                view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    onCalendarPick(LocalDate.of(year, month + 1, dayOfMonth))
                }
            }
        )
    }
}

@Composable
fun HourOfTheDayItemComposable(
    modifier: Modifier = Modifier,
    taskModel: TaskPresentationModel,
    onTaskClick: (Int) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 7.dp, horizontal = 5.dp)
        ) {
            Text(text = taskModel.durationAsString())

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                taskModel.taskList.mapIndexed { index, task ->
                    ClickableText(
                        text = AnnotatedString("${index + 1}. ${task.name}"),
                        onClick = { onTaskClick(task.id) },
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(5.dp),
                                color = Color.LightGray
                            )
                            .padding(5.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    TaskSelectScreenComposable(
        taskModelArray = tasksArray,
        onCalendarPick = {},
        onTaskClick = {},
        chosenDay = LocalDatePresentationModel(LocalDate.now()),
        onNewTaskClick = {})
}

val tasksArray = arrayOf(
    TaskPresentationModel(
        timeStart = LocalTime.of(0, 0),
        timeEnd = LocalTime.of(1, 0),
        taskList = listOf(
            Task(
                name = "First",
                dateEnd = LocalDateTime.now(),
                dateStart = LocalDateTime.now(),
                description = "wewewewewewew",
                id = 1
            ),
            Task(
                name = "Lorem ipsum lorem ipsum lorem ipsum lorem ipsum",
                dateEnd = LocalDateTime.now(),
                dateStart = LocalDateTime.now(),
                description = "wewewewewewew",
                id = 3
            )
        )
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(1, 0),
        timeEnd = LocalTime.of(2, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(2, 0),
        timeEnd = LocalTime.of(3, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(3, 0),
        timeEnd = LocalTime.of(4, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(4, 0),
        timeEnd = LocalTime.of(5, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(5, 0),
        timeEnd = LocalTime.of(6, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(6, 0),
        timeEnd = LocalTime.of(7, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(7, 0),
        timeEnd = LocalTime.of(8, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(8, 0),
        timeEnd = LocalTime.of(9, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(9, 0),
        timeEnd = LocalTime.of(10, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(10, 0),
        timeEnd = LocalTime.of(11, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(11, 0),
        timeEnd = LocalTime.of(12, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(12, 0),
        timeEnd = LocalTime.of(13, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(13, 0),
        timeEnd = LocalTime.of(14, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(14, 0),
        timeEnd = LocalTime.of(15, 0),
    ), TaskPresentationModel(
        timeStart = LocalTime.of(15, 0),
        timeEnd = LocalTime.of(16, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(16, 0),
        timeEnd = LocalTime.of(17, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(17, 0),
        timeEnd = LocalTime.of(18, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(18, 0),
        timeEnd = LocalTime.of(19, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(19, 0),
        timeEnd = LocalTime.of(20, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(20, 0),
        timeEnd = LocalTime.of(21, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(21, 0),
        timeEnd = LocalTime.of(22, 0),
    ),
    TaskPresentationModel(
        timeStart = LocalTime.of(22, 0),
        timeEnd = LocalTime.of(23, 0),
    ), TaskPresentationModel(
        timeStart = LocalTime.of(23, 0),
        timeEnd = LocalTime.of(23, 59),
    )
)