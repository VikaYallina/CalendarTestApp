package com.yallina.myapplication.presentation.task_select_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yallina.myapplication.MyApplication
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.GetTasksOnDayUseCase
import com.yallina.myapplication.presentation.task_select_screen.model.TaskPresentationModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import javax.inject.Inject

class TaskSelectViewModel : ViewModel() {

    @Inject
    lateinit var getTasksOnDay: GetTasksOnDayUseCase

    init {
        MyApplication.get().injector.inject(this)
    }

    private val _taskPresentationArray =
        MutableLiveData<Array<TaskPresentationModel>>(initPresentationArray())
    val taskPresentationArray: LiveData<Array<TaskPresentationModel>> = _taskPresentationArray

    private val _chosenDate = MutableLiveData<LocalDate>(LocalDate.now())
    val chosenDate: LiveData<LocalDate> = _chosenDate

    fun initPresentationArray(): Array<TaskPresentationModel> {
        return Array(24) { i ->
            if (i != 23){
                TaskPresentationModel(timeStart = LocalTime.of(i, 0), timeEnd = LocalTime.of(i + 1, 0))
            }else {
                TaskPresentationModel(timeStart = LocalTime.of(i, 0), timeEnd = LocalTime.of(i , 59))
            }
        }
    }

    fun onDayChosen() {

    }

    fun createPresentationArray(tasks: List<Task>, day: LocalDate) {
        _taskPresentationArray.postValue(initPresentationArray())
    }
}

//class TaskSelectViewModelFactory(
//    private val getTasksOnDay: GetTasksOnDayUseCase
//) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>) = TaskSelectViewModel(getTasksOnDay) as T
//}