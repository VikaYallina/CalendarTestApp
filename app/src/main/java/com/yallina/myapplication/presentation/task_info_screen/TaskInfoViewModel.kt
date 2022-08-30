package com.yallina.myapplication.presentation.task_info_screen

import android.util.Log
import androidx.lifecycle.*
import com.yallina.myapplication.domain.use_case.GetTaskByIdUseCase
import com.yallina.myapplication.presentation.task_info_screen.mapper.toInfoPresentation
import com.yallina.myapplication.presentation.task_info_screen.model.TaskInfoPresentationModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yallina.myapplication.domain.model.Task


/**
 * [ViewModel] that is used to retrieve [Task] by taskId from repository and convert it into
 * [TaskInfoPresentationModel]
 */
class TaskInfoViewModel @Inject constructor(
    private var getTaskByIdUseCase: GetTaskByIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {

//    @Inject
//    lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

//    init {
//        MyApplication.get().injector.inject(this)
//    }

    private val _task = MutableLiveData<TaskInfoPresentationModel>()
    val task: LiveData<TaskInfoPresentationModel> = _task

    /**
     * Retrieve [Task] from repository in coroutine scope, convert it to
     * [TaskInfoPresentationModel] nad post to [LiveData]
     */
    fun showTaskById(id: Int){
        viewModelScope.launch {
            getTaskByIdUseCase.execute(id)
                .map { it.toInfoPresentation() }
                .collect { task ->
                    _task.value = task
                }
        }
    }

    override fun onCleared() {
        Log.i(this::class.java.simpleName, "onCleared")
        super.onCleared()
    }
}

class TaskInfoViewModelFactory(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = TaskInfoViewModel(getTaskByIdUseCase) as T
}
