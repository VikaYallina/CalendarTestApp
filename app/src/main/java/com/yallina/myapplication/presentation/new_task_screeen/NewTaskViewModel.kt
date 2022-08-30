package com.yallina.myapplication.presentation.new_task_screeen

import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.AddNewTaskUseCase
import com.yallina.myapplication.presentation.new_task_screeen.model.NewTaskPresentationModel
import com.yallina.myapplication.utils.MyDateTimeFormatter
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import com.yallina.myapplication.domain.model.Result
import com.yallina.myapplication.presentation.task_select_screen.mapper.toDomainModel

class NewTaskViewModel(
    private val addNewTaskUseCase: AddNewTaskUseCase
) : ViewModel() {

    private val _isSaveSuccessful = MutableLiveData(false)

    /**
     * [LiveData] that emits true if task save was successful
     */
    val isSaveSuccessful: LiveData<Boolean>
        get() = _isSaveSuccessful


    private val _snackbar = MutableLiveData<String?>()

    /**
     * [LiveData] that emits event messages that need to be shown in a [Snackbar]
     */
    val snackbar: LiveData<String?>
        get() = _snackbar

    /**
     * Called whenever the ui shows the [Snackbar]. The value emitted by [snackbar] LiveData is emptied
     */
    fun onSnackbarShow() {
        _snackbar.value = null
    }


    private val _dateStartString = MutableLiveData<String?>()

    /**
     * [LiveData] that emits the String representation of the dateStart date chosen by user
     */
    val dateStartString: LiveData<String?>
        get() = _dateStartString


    private val _dateEndString = MutableLiveData<String?>()

    /**
     * [LiveData] that emits the String representation of the dateEnd date chosen by user
     */
    val dateEndString: LiveData<String?>
        get() = _dateEndString


    // All user input will be held in this presentation model
    // And will be passed to saveTask method when user chooses to save the task
    private val newTask: NewTaskPresentationModel = NewTaskPresentationModel()

    fun setNewTaskName(name: String) {
        newTask.name = name
    }

    fun setNewTaskDescription(desc: String) {
        newTask.description = desc
    }

    fun setNewTaskDateStart(date: LocalDateTime) {
        newTask.dateStart = date
        _dateStartString.value = date.format(MyDateTimeFormatter.formatter)
    }

    fun setNewTaskDateEnd(date: LocalDateTime) {
        newTask.dateEnd = date
        _dateEndString.value = date.format(MyDateTimeFormatter.formatter)
    }


    /**
     * Handles save task button click event
     */
    fun onSaveButtonClick() {
        if (validateTask())
            saveTask(newTask)
    }


    /**
     * Check the correctness of input values in [NewTaskPresentationModel]
     */
    private fun validateTask(): Boolean {
        if (newTask.name.isNullOrBlank()) {
            _snackbar.value = "Введите название задания"
            return false
        }

        if (newTask.description.isNullOrBlank()) {
            _snackbar.value = "Введите описание задания"
            return false
        }

        if (newTask.dateStart == null) {
            _snackbar.value = "Выберите день начала задания"
            return false
        }

        if (newTask.dateEnd == null) {
            _snackbar.value = "Выберите день окончания задания"
            return false
        }

        if (newTask.dateStart!!.isAfter(newTask.dateEnd)) {
            _snackbar.value = "День окончания не может быть раньше начала"
            return false
        }

        return true
    }

    /**
     * Attempt to save new [Task] to persistence storage. Depending on the [Result] of the operation
     * ether notifies [isSaveSuccessful] of a success or provides [snackbar] with error message
     * @param newTask a [NewTaskPresentationModel] object that need to be saved in a persistence storage
     */
    private fun saveTask(newTask: NewTaskPresentationModel) {
        viewModelScope.launch {
            try {
                val taskModel = newTask.toDomainModel()
                val result = addNewTaskUseCase.execute(taskModel)

                when (result) {
                    is Result.Success<Task> -> _isSaveSuccessful.value = true
                    is Result.Error -> _snackbar.value = result.exception.message
                }
            } catch (e: Throwable) {
                _snackbar.value = e.message
            }
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}

class NewTaskViewModelFactory(
    private val addNewTaskUseCase: AddNewTaskUseCase,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        NewTaskViewModel(addNewTaskUseCase) as T
}
