package com.yallina.myapplication.presentation.new_task_screeen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yallina.myapplication.MyApplication
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.AddNewTaskUseCase
import com.yallina.myapplication.presentation.new_task_screeen.model.NewTaskPresentationModel
import com.yallina.myapplication.utils.MyDateTimeFormatter
import com.yallina.myapplication.utils.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import com.yallina.myapplication.domain.model.Result

class NewTaskViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {
    private val TAG = this::class.java.simpleName

    @Inject
    lateinit var addNewTaskUseCase: AddNewTaskUseCase

    init {
        MyApplication.get().injector.inject(this)
    }

    private val _isSaveSuccessful = MutableLiveData(false)
    val isSaveSuccessful: LiveData<Boolean>
        get() = _isSaveSuccessful

    private val _snackbar = MutableLiveData<String?>()
    val snackbar: LiveData<String?>
        get() = _snackbar

    private val _dateStartString = MutableLiveData<String?>()
    val dateStartString: LiveData<String?>
        get() = _dateStartString

    private val _dateEndString = MutableLiveData<String?>()
    val dateEndString: LiveData<String?>
        get() = _dateEndString

    fun onSnackbarShow(){
        _snackbar.value = null
    }

    private val newTask: NewTaskPresentationModel = NewTaskPresentationModel()

    fun setNewTaskName(name: String){
        newTask.name = name
    }

    fun setNewTaskDescription(desc: String){
        newTask.description = desc
    }

    fun setNewTaskDateStart(date: LocalDateTime){
        newTask.dateStart = date
        _dateStartString.value = date.format(MyDateTimeFormatter.formatter)
    }

    fun setNewTaskDateEnd(date: LocalDateTime){
        newTask.dateEnd = date
        _dateEndString.value = date.format(MyDateTimeFormatter.formatter)
    }

    fun validateTask(){
        if (newTask.name.isNullOrBlank()){
            _snackbar.value = "Введите название задания"
            return
        }

        if (newTask.description.isNullOrBlank()){
            _snackbar.value = "Введите описание задания"
            return
        }

        if (newTask.dateStart == null){
            _snackbar.value = "Выберите день начала задания"
            return
        }

        if (newTask.dateEnd == null){
            _snackbar.value = "Выберите день окончания задания"
            return
        }

        if (newTask.dateStart!!.isAfter(newTask.dateEnd)){
            _snackbar.value = "День окончания не может быть раньше начала"
            return
        }

        saveTask(newTask)
    }

    private fun saveTask(newTask: NewTaskPresentationModel) {
        viewModelScope.launch {
            try {
                val taskModel = newTask.toDomainModel()
                val result = addNewTaskUseCase.execute(taskModel)

                when(result){
                    is Result.Success<Task> -> _isSaveSuccessful.value = true
                    is Result.Error -> _snackbar.value = result.exception.message
                }
            }catch (e: Throwable){
                _snackbar.value = e.message
            }
        }
    }

}