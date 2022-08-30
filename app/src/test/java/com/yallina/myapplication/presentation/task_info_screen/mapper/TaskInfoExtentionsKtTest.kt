package com.yallina.myapplication.presentation.task_info_screen.mapper

import com.yallina.myapplication.presentation.task_info_screen.mapper.test_cases.TaskInfoMapperTestCases.customFormatter
import com.yallina.myapplication.presentation.task_info_screen.mapper.test_cases.TaskInfoMapperTestCases.customPresentationTask
import com.yallina.myapplication.presentation.task_info_screen.mapper.test_cases.TaskInfoMapperTestCases.defaultPresentationTask
import com.yallina.myapplication.presentation.task_info_screen.mapper.test_cases.TaskInfoMapperTestCases.isoLocalDateTimeFormatter
import com.yallina.myapplication.presentation.task_info_screen.mapper.test_cases.TaskInfoMapperTestCases.isoPresentationTask
import com.yallina.myapplication.presentation.task_info_screen.mapper.test_cases.TaskInfoMapperTestCases.testTask
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

internal class TaskInfoExtentionsKtTest {

    @Test
    fun testDefault() {
        /* Given */
        val task = testTask

        /* When */
        val result = task.toInfoPresentation()

        /* Then */
        assertThat(result, `is`(defaultPresentationTask))
    }

    @Test
    fun testWithIsoLocalDateTimeFormatter() {
        /* Given */
        val task = testTask

        /* When */
        val result = task.toInfoPresentation(isoLocalDateTimeFormatter)

        /* Then */
        assertThat(result, `is`(isoPresentationTask))
    }

    @Test
    fun testWithCustomLocalDateTimeFormatter() {
        /* Given */
        val task = testTask

        /* When */
        val result = task.toInfoPresentation(customFormatter)

        /* Then */
        assertThat(result, `is`(customPresentationTask))
    }

}