package com.yallina.myapplication.presentation.task_info_screen

import androidx.lifecycle.*
import com.yallina.myapplication.MyApplication
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.use_case.GetTaskByIdUseCase
import com.yallina.myapplication.presentation.task_info_screen.model.TaskInfoPresentationModel
import com.yallina.myapplication.utils.toInfoPresentation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskInfoViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {

    @Inject
    lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    init {
        MyApplication.get().injector.inject(this)
    }

    private val _task = MutableLiveData<TaskInfoPresentationModel>()
    val task: LiveData<TaskInfoPresentationModel> = _task

    fun showTaskById(id: Int){
        viewModelScope.launch(defaultDispatcher) {
            getTaskByIdUseCase.execute(id)
                .map { it.toInfoPresentation() }
                .collect {
                    _task.postValue(it)
                }
        }
    }
}