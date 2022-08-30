package com.yallina.myapplication.presentation.new_task_screeen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yallina.myapplication.domain.model.Result
import com.yallina.myapplication.domain.repository.TasksRepository
import com.yallina.myapplication.domain.use_case.AddNewTaskUseCase
import com.yallina.myapplication.presentation.new_task_screeen.test_data.NewTaskViewModelTestData.taskBlankDescription
import com.yallina.myapplication.presentation.new_task_screeen.test_data.NewTaskViewModelTestData.taskEndDateBeforeStart
import com.yallina.myapplication.presentation.new_task_screeen.test_data.NewTaskViewModelTestData.taskSaveErrorResult
import com.yallina.myapplication.presentation.new_task_screeen.test_data.NewTaskViewModelTestData.taskSaveSuccessResult
import com.yallina.myapplication.test_utils.MainCoroutineRule
import com.yallina.myapplication.test_utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import java.util.concurrent.TimeoutException

@RunWith(MockitoJUnitRunner::class)
internal class NewTaskViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var tasksRepositoryMock: TasksRepository

    private lateinit var addNewTaskUseCase: AddNewTaskUseCase

    private lateinit var newTaskViewModel: NewTaskViewModel


    @Before
    fun initBeforeTest() {
        tasksRepositoryMock = mock() {
            onBlocking { addTask(taskSaveSuccessResult) } doReturn Result.Success()
            onBlocking { addTask(taskSaveErrorResult) } doReturn Result.Error(Exception("Exception occurred"))
        }
        addNewTaskUseCase = AddNewTaskUseCase(tasksRepositoryMock)
        newTaskViewModel = NewTaskViewModel(addNewTaskUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSave_WithEmptyTaskObject_ExpectOnlySnackbarMessage() = runTest {
        /* Given */

        /* When */
        newTaskViewModel.onSaveButtonClick()

        /* Then */
        verify(tasksRepositoryMock, never()).addTask(any())

        val snackBarMessage = newTaskViewModel.snackbar.getOrAwaitValue()
        assertThat(snackBarMessage, `is`("Введите название задания"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSave_WithAllFieldsFilledExceptDesc_ExpectOnlySnackbarMessage() = runTest {
        /* Given */
        newTaskViewModel.setNewTaskName(taskBlankDescription.name)
        newTaskViewModel.setNewTaskDescription(taskBlankDescription.description)
        newTaskViewModel.setNewTaskDateStart(taskBlankDescription.dateStart)
        newTaskViewModel.setNewTaskDateEnd(taskBlankDescription.dateEnd)

        /* When */
        newTaskViewModel.onSaveButtonClick()

        /* Then */
        verify(tasksRepositoryMock, never()).addTask(any())

        val snackBarMessage = newTaskViewModel.snackbar.getOrAwaitValue()
        assertThat(snackBarMessage, `is`("Введите описание задания"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSave_WithEndDateBeforeStartDate_ExpectOnlySnackbarMessage() = runTest {
        /* Given */
        newTaskViewModel.setNewTaskName(taskEndDateBeforeStart.name)
        newTaskViewModel.setNewTaskDescription(taskEndDateBeforeStart.description)
        newTaskViewModel.setNewTaskDateStart(taskEndDateBeforeStart.dateStart)
        newTaskViewModel.setNewTaskDateEnd(taskEndDateBeforeStart.dateEnd)

        /* When */
        newTaskViewModel.onSaveButtonClick()

        /* Then */
        verify(tasksRepositoryMock, never()).addTask(any())

        val snackBarMessage = newTaskViewModel.snackbar.getOrAwaitValue()
        assertThat(snackBarMessage, `is`("День окончания не может быть раньше начала"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSave_RepositorySaveSuccess_ExpectLiveDataSuccess() = runTest {
        /* Given */
        newTaskViewModel.setNewTaskName(taskSaveSuccessResult.name)
        newTaskViewModel.setNewTaskDescription(taskSaveSuccessResult.description)
        newTaskViewModel.setNewTaskDateStart(taskSaveSuccessResult.dateStart)
        newTaskViewModel.setNewTaskDateEnd(taskSaveSuccessResult.dateEnd)

        /* When */
        newTaskViewModel.onSaveButtonClick()

        /* Then */
        advanceUntilIdle()

        verify(tasksRepositoryMock).addTask(taskSaveSuccessResult)

        assertThrows<TimeoutException>() {
            newTaskViewModel.snackbar.getOrAwaitValue()
        }

        val isSaveSuccessful = newTaskViewModel.isSaveSuccessful.getOrAwaitValue()
        assertThat(isSaveSuccessful, `is`(true))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSave_RepositorySaveError_ExpectSnackbarMessage() = runTest {
        /* Given */
        newTaskViewModel.setNewTaskName(taskSaveErrorResult.name)
        newTaskViewModel.setNewTaskDescription(taskSaveErrorResult.description)
        newTaskViewModel.setNewTaskDateStart(taskSaveErrorResult.dateStart)
        newTaskViewModel.setNewTaskDateEnd(taskSaveErrorResult.dateEnd)

        /* When */
        newTaskViewModel.onSaveButtonClick()

        /* Then */
        advanceUntilIdle()

        verify(tasksRepositoryMock).addTask(taskSaveErrorResult)

        val snackbarMessage = newTaskViewModel.snackbar.getOrAwaitValue()
        assertThat(snackbarMessage, `is`("Exception occurred"))

        val isSaveSuccessful = newTaskViewModel.isSaveSuccessful.getOrAwaitValue()
        assertThat(isSaveSuccessful, `is`(false))
    }
}