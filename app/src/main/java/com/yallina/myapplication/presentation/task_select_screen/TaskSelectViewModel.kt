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

    lateinit var job: Job

    private var observer: Observer<LocalDatePresentationModel> =
        Observer<LocalDatePresentationModel> { dateModel ->
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

    private var initialPresentationArray: Array<TaskPresentationModel>

    private val _taskPresentationArray = MutableLiveData<Array<TaskPresentationModel>>(emptyArray())

    /**
     * [LiveData] that emits an array of [TaskPresentationModel] whenever a user chooses new date
     */
    val taskPresentationArray: LiveData<Array<TaskPresentationModel>>
        get() = _taskPresentationArray


    /**
     *  Creates an array of [TaskPresentationModel] with size of 24, corresponding to each hour of the day.
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

    init {
        try {
            val inputStream = context.assets.open("tasks.json")
            initializeDataBase(inputStream)
        } catch (e: IOException) {
            _snackbar.value = "Exception while opening asset file"
        }

        initialPresentationArray = initPresentationArray()

        // Creates a new Flow from a database request whenever user chooses a date
        chosenDate.observeForever(observer)
    }

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


    /**
     *
     */
    private fun createPresentationArray(
        tasks: List<Task>,
        day: LocalDate?
    ): Array<TaskPresentationModel> {
        val tasksPresentArray = initialPresentationArray.copyOf()
        if (day == null) return tasksPresentArray

        val array = Array(HOURS_IN_A_DAY) {
            mutableListOf<Task>()
        }

        tasks.forEach { task ->
            val startIndex =
                if (task.dateStart.isBefore(day.atStartOfDay())) STARTING_HOUR_OF_DAY
                else task.dateStart.hour
            val endIndex =
                if (task.dateEnd.isAfter(day.atStartOfDay().plusDays(1))) FINAL_HOUR_OF_DAY
                else task.dateEnd.hour

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

    companion object {
        private val TAG = this::class.java.simpleName

        private const val HOURS_IN_A_DAY = 24
        private const val STARTING_HOUR_OF_DAY = 0
        private const val FINAL_HOUR_OF_DAY = 23
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