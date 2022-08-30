package com.yallina.myapplication.presentation.task_select_screen

import android.content.Context
import android.widget.CalendarView
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.GetTasksOnDayUseCase
import com.yallina.myapplication.domain.use_case.InitDataFromFileUseCase
import com.yallina.myapplication.presentation.task_select_screen.model.LocalDatePresentationModel
import com.yallina.myapplication.presentation.task_select_screen.model.TaskPresentationModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import java.io.InputStream
import com.yallina.myapplication.domain.model.Result
import java.io.IOException

class TaskSelectViewModel(
    private val getTasksOnDay: GetTasksOnDayUseCase,
    private val initDataFromFileUseCase: InitDataFromFileUseCase,
    context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _taskPresentationArray = MutableLiveData<Array<TaskPresentationModel>>(emptyArray())

    /**
     * [LiveData] that emits an array of [TaskPresentationModel] whenever a user chooses new date
     */
    val taskPresentationArray: LiveData<Array<TaskPresentationModel>>
        get() = _taskPresentationArray


    private val _chosenDate = MutableLiveData(LocalDatePresentationModel(LocalDate.now()))

    /**
     * This [LiveData] is updated whenever user clicks on a different date in a [CalendarView]
     */
    val chosenDate: LiveData<LocalDatePresentationModel>
        get() = _chosenDate


    private val _isLoading = MutableLiveData(false)

    /**
     * [LiveData] that emits true when some long running operation starts
     * and emits false when this operation stops
     */
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    private val _snackbar = MutableLiveData<String?>()

    /**
     * Sets a String to show in a [Snackbar]
     */
    val snackbar: LiveData<String?>
        get() = _snackbar


    /**
     * Called immediately after [Snackbar] is shown to the user.
     * Resets the value of the [snackbar] livedata
     */
    fun onSnackbarShow() {
        _snackbar.value = null
    }


    private var initialPresentationArray: Array<TaskPresentationModel>

    /**
     *  Creates an array of [TaskPresentationModel] with size of [HOURS_IN_A_DAY], corresponding to each hour of the day.
     *  The tasks list for each hour is empty.
     *  @return
     */
    private fun initPresentationArray(): Array<TaskPresentationModel> {
        return Array(HOURS_IN_A_DAY) { i ->
            if (i != FINAL_HOUR_OF_DAY) {
                TaskPresentationModel(
                    timeStart = LocalTime.of(i, 0),
                    timeEnd = LocalTime.of(i + 1, 0)
                )
            } else {
                TaskPresentationModel(timeStart = LocalTime.of(i, 0), timeEnd = LocalTime.of(i, 59))
            }
        }
    }


    /**
     * Holds a coroutine job of the current flow. It will be cancelled before a new data request is started
     */
    private lateinit var currentFlowJob: Job

    private var observer: Observer<LocalDatePresentationModel> =
        Observer<LocalDatePresentationModel> { dateModel ->
            // We should cancel the previous flow if it is still active
            if (this::currentFlowJob.isInitialized && currentFlowJob.isActive)
                currentFlowJob.cancel()

            currentFlowJob = getTasksOnDay.execute(dateModel.date)
                .distinctUntilChanged()
                .onEach { list ->
                    handleFlowItem(list)
                }
                .catch { e -> _snackbar.value = e.message }
                .cancellable()
                .launchIn(viewModelScope)
        }


    init {
        try {
            val inputStream = context.assets.open(ASSET_TASK_FILENAME)
            initializeDataBase(inputStream)
        } catch (e: IOException) {
            _snackbar.value = "Exception while opening asset file"
        }

        initialPresentationArray = initPresentationArray()

        // Creates a new Flow from a database request whenever user chooses a date
        chosenDate.observeForever(observer)
    }


    /**
     * Initialize database with tasks from assets file
     * @param inputStream data stream from assets file
     */
    private fun initializeDataBase(inputStream: InputStream) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val initResult = initDataFromFileUseCase.execute(inputStream)

                when (initResult) {
                    is Result.Success -> _isLoading.value = false
                    is Result.Error -> _snackbar.value = initResult.exception.message
                }
            } catch (t: Throwable) {
                _snackbar.value = t.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    /**
     * Updates the [chosenDate] livedata if user chose a new date
     */
    fun onDayChosen(day: LocalDate) {
        if (!day.isEqual(chosenDate.value?.date)) {
            _chosenDate.postValue(LocalDatePresentationModel(day))
        }
    }


    private suspend fun handleFlowItem(list: List<Task>) {
        try {
            _isLoading.value = true
            val res = withContext(defaultDispatcher) {
                updatePresentationArray(list, chosenDate.value?.date)
            }
            _taskPresentationArray.value = res
        } catch (e: Throwable) {
            _snackbar.value = e.message
        } finally {
            _isLoading.value = false
        }

    }


    /**
     * Update an array of [TaskPresentationModel] with new data
     * @param tasks list of tasks that were received from a repository request
     * @param day day that was chosen by the user
     * @return array of [TaskPresentationModel]
     */
    private fun updatePresentationArray(
        tasks: List<Task>,
        day: LocalDate?
    ): Array<TaskPresentationModel> {
        val tasksPresentArray = initialPresentationArray.copyOf()
        if (day == null) return tasksPresentArray

        // Holds a list of tasks for each hour of the day
        val arrayOfTasksPerHour = Array(HOURS_IN_A_DAY) {
            mutableListOf<Task>()
        }

        tasks.forEach { task ->
            val startIndex =
                if (task.dateStart.isBefore(day.atStartOfDay())) STARTING_HOUR_OF_DAY
                else task.dateStart.hour
            val endIndex =
                if (task.dateEnd.isAfter(day.atStartOfDay().plusDays(1))) FINAL_HOUR_OF_DAY
                else task.dateEnd.hour

            // Populate all lists in the arrayOfTasksPerHour on hours that this task occurs
            if (startIndex == endIndex)
                arrayOfTasksPerHour[startIndex].add(task)
            else
                for (i in startIndex..endIndex)
                    arrayOfTasksPerHour[i].add(task)
        }

        tasksPresentArray.forEachIndexed { index, taskPresentModel ->
            taskPresentModel.taskList = arrayOfTasksPerHour[index]
        }
        return tasksPresentArray
    }

    override fun onCleared() {
        chosenDate.removeObserver(observer)
        super.onCleared()
    }

    companion object {
        private val TAG = this::class.java.simpleName

        private const val HOURS_IN_A_DAY = 24
        private const val STARTING_HOUR_OF_DAY = 0
        private const val FINAL_HOUR_OF_DAY = 23

        private const val ASSET_TASK_FILENAME = "tasks.json"
    }
}

class TaskSelectViewModelFactory(
    private val getTasksOnDay: GetTasksOnDayUseCase,
    private val initDataFromFileUseCase: InitDataFromFileUseCase,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TaskSelectViewModel(getTasksOnDay, initDataFromFileUseCase, context) as T
}