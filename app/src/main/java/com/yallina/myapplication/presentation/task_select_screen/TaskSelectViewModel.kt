package com.yallina.myapplication.presentation.task_select_screen

import android.util.Log
import android.widget.CalendarView
import androidx.lifecycle.*
import com.yallina.myapplication.MyApplication
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.GetTasksOnDayUseCase
import com.yallina.myapplication.presentation.task_select_screen.model.LocalDatePresentationModel
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

    lateinit var job: Job

    private var observer: Observer<LocalDatePresentationModel> = Observer<LocalDatePresentationModel> { dateModel ->
        // We should cancel the previous flow if it is still active
        if (this::job.isInitialized && job.isActive)
            job.cancel()

        job = getTasksOnDay.execute(dateModel.date)
            .distinctUntilChanged()
            .onEach { list ->
                handleFlowItem(list)
            }
            .catch { e -> _snackbar.value = e.message }
            .cancellable()
            .launchIn(viewModelScope)
    }

    private val _taskPresentationArray =
        MutableLiveData<Array<TaskPresentationModel>>(initPresentationArray())
    val taskPresentationArray: LiveData<Array<TaskPresentationModel>>
        get() = _taskPresentationArray


    private val _chosenDate = MutableLiveData(LocalDatePresentationModel(LocalDate.now()))
    /**
     * This [LiveData] is updated whenever user clicks on a different date in a [CalendarView]
     */
    val chosenDate: LiveData<LocalDatePresentationModel>
        get() = _chosenDate


    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    private val _snackbar = MutableLiveData<String?>()
    /**
     * Sets a String to show in a [Snackbar]
     */
    val snackbar: LiveData<String?>
        get() = _snackbar

    /**
     * Called immediately after [Snackbar] is shown to the user
     * Resets the value of the [snackbar] livedata
     */
    fun onSnackbarShow() {
        _snackbar.value = null
    }

    init {
        MyApplication.get().injector.inject(this)

        // Creates a new Flow from a database request whenever user chooses a date
        chosenDate.observeForever(observer)
    }

    /**
     *  Creates an array of [TaskPresentationModel] with size of 24, corresponding to each hour of the day.
     *  The tasks list for each hour is empty.
     *  @return
     */
    private fun initPresentationArray(): Array<TaskPresentationModel> {
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


    fun onDayChosen(day: LocalDate) {
        if (!day.isEqual(chosenDate.value?.date)) {
            _chosenDate.postValue(LocalDatePresentationModel(day))
        }
    }

    private suspend fun handleFlowItem(list: List<Task>) {
        try {
            _isLoading.value = true
            val res = withContext(defaultDispatcher) {
                createPresentationArray(list, chosenDate.value?.date)
            }
            _taskPresentationArray.value = res
        } catch (e: Throwable) {
            _snackbar.value = e.message
        } finally {
            _isLoading.value = false
        }

    }


    private fun createPresentationArray(
        tasks: List<Task>,
        day: LocalDate?
    ): Array<TaskPresentationModel> {
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