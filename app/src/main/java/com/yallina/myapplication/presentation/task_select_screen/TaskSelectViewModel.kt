package com.yallina.myapplication.presentation.task_select_screen

import android.util.Log
import androidx.lifecycle.*
import com.yallina.myapplication.MyApplication
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.GetTasksOnDayUseCase
import com.yallina.myapplication.presentation.task_select_screen.model.TaskPresentationModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TaskSelectViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val TAG = this::class.java.simpleName

    @Inject
    lateinit var getTasksOnDay: GetTasksOnDayUseCase

    init {
        MyApplication.get().injector.inject(this)
    }

    private val initialArray = initPresentationArray()

    private val _taskPresentationArray = MutableLiveData(initialArray)
    val taskPresentationArray: LiveData<Array<TaskPresentationModel>>
        get() = _taskPresentationArray

    private val _chosenDate = MutableLiveData(LocalDate.now())
    val chosenDate: LiveData<LocalDate>
        get() = _chosenDate

    private fun initPresentationArray(): Array<TaskPresentationModel> {
        Log.i(TAG, "init array")
        return Array(24) { i ->
            if (i != 23) {
                TaskPresentationModel(
                    timeStart = LocalTime.of(i, 0),
                    timeEnd = LocalTime.of(i + 1, 0)
                )
            } else {
                TaskPresentationModel(timeStart = LocalTime.of(i, 0), timeEnd = LocalTime.of(i, 59))
            }
        }
    }

    lateinit var job: Job

//    fun onDayChosen() {
//        if (this::job.isInitialized) {
//            job.cancel()
//        }
//        job = viewModelScope.launch {
//            chosenDate.value?.let {
//                val flow = getTasksOnDay.execute(it)
//                flow.onCompletion {
//                    Log.i(TAG, "Completion")
//                    Log.i(TAG, Thread.currentThread().name)
//                }
//                    .cancellable()
//                    .collect { list ->
//                        Log.i(TAG, list.toString())
//                        Log.i(TAG, Thread.currentThread().name)
//                    }
//
//            }
//        }
//    }

    private lateinit var taskFlow: Flow<List<Task>>

    fun onDayChosen(day: LocalDate) {
        if (!day.isEqual(chosenDate.value)) {
            getTasksOnDay.execute(day)
                .distinctUntilChanged()
                .onCompletion {
                    Log.i(TAG, "Completion, ${Thread.currentThread().name}")
                }
                .takeWhile {
                    chosenDate.value == day
                }
                .catch { e -> _snackbar.value = e.message }
                .onEach { list ->
                    Log.i(TAG, list.toString() + Thread.currentThread().name)
                    val value = createPresentationArray(list, day)
                    withContext(Dispatchers.Main){
                        _taskPresentationArray.value = value
                    }
                }
                .flowOn(defaultDispatcher)
                .launchIn(viewModelScope)
        }
        _chosenDate.postValue(day)

//        collectTaskFlow(day)
    }

    private val _snackbar = MutableLiveData<String?>()
    val snackbar: LiveData<String?>
        get() = _snackbar

    private fun collectTaskFlow(day: LocalDate) {
        viewModelScope.launch(defaultDispatcher) {
            try {

            } catch (e: Throwable) {
                _snackbar.value = e.message
            }
        }
    }

    private suspend fun createPresentationArray(
        tasks: List<Task>,
        day: LocalDate
    ): Array<TaskPresentationModel> {

        val tasksPresentArray = taskPresentationArray.value ?: emptyArray()

        val array = Array(24) {
            mutableListOf<Task>()
        }

        tasks.forEach { task ->
            val startIndex =
                if (task.dateStart.isBefore(day.atStartOfDay())) 0 else task.dateStart.hour
            val endIndex =
                if (task.dateEnd.isAfter(day.atStartOfDay().plusDays(1))) 0 else task.dateEnd.hour

            if (startIndex == endIndex)
                array[startIndex].add(task)
            else
                for (i in startIndex..endIndex)
                    array[i].add(task)
        }

        tasksPresentArray.forEachIndexed { index, taskPresentModel ->
            taskPresentModel.taskList = array[index]
        }
        return tasksPresentArray
    }

}

//class TaskSelectViewModelFactory(
//    private val getTasksOnDay: GetTasksOnDayUseCase
//) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>) = TaskSelectViewModel(getTasksOnDay) as T
//}