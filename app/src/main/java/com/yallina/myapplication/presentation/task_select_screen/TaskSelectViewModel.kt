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

    private var observer: Observer<LocalDate> = Observer<LocalDate> { date ->
        if (this::job.isInitialized && job.isActive)
            job.cancel()

        job = getTasksOnDay.execute(date)
            .distinctUntilChanged()
            .onEach { list ->
                Log.i(TAG, list.toString())
                handleFlowItem(list)
            }
            .onCompletion {
                Log.i(TAG, "Completion, ${Thread.currentThread().name}")
            }
            .catch { e -> _snackbar.value = e.message }
            .cancellable()
            .launchIn(viewModelScope)
    }

    private val _taskPresentationArray =
        MutableLiveData<Array<TaskPresentationModel>>(initPresentationArray())
    val taskPresentationArray: LiveData<Array<TaskPresentationModel>>
        get() = _taskPresentationArray

    private val _chosenDate = MutableLiveData(LocalDate.now())
    val chosenDate: LiveData<LocalDate>
        get() = _chosenDate

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    lateinit var job: Job

    init {
        Log.i(TAG, "init")
        MyApplication.get().injector.inject(this)

//        observer =
        chosenDate.observeForever(observer)
    }


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

        }
        _chosenDate.postValue(day)

    }

    private suspend fun handleFlowItem(list: List<Task>) {
        try {
            _isLoading.value = true
            val res = withContext(defaultDispatcher) {
                createPresentationArray(list, chosenDate.value)
            }
            _taskPresentationArray.value = res
        } catch (e: Throwable) {
            _snackbar.value = e.message
        } finally {
            _isLoading.value = false
        }

    }

    private val _snackbar = MutableLiveData<String?>()
    val snackbar: LiveData<String?>
        get() = _snackbar

    fun onSnackbarShow() {
        _snackbar.value = null
    }


    private fun createPresentationArray(
        tasks: List<Task>,
        day: LocalDate?
    ): Array<TaskPresentationModel> {
        Log.i(TAG, "createPresentationArray")

        val tasksPresentArray = taskPresentationArray.value ?: return emptyArray()
        if (day == null) return emptyArray()

        val array = Array(24) {
            mutableListOf<Task>()
        }

        tasks.forEach { task ->
            val startIndex =
                if (task.dateStart.isBefore(day.atStartOfDay())) 0 else task.dateStart.hour
            val endIndex =
                if (task.dateEnd.isAfter(day.atStartOfDay().plusDays(1))) 23 else task.dateEnd.hour

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

    override fun onCleared() {
        chosenDate.removeObserver(observer)
        super.onCleared()
    }

}

//class TaskSelectViewModelFactory(
//    private val getTasksOnDay: GetTasksOnDayUseCase
//) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>) = TaskSelectViewModel(getTasksOnDay) as T
//}