package com.yallina.myapplication.presentation.new_task_screeen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yallina.myapplication.R
import com.yallina.myapplication.data.local_db.converter.LocalDateTimeConverter
import org.threeten.bp.LocalDateTime
import java.util.*

class NewTaskFragment : Fragment(R.layout.new_task_fragment) {
    private val newTaskViewModel by viewModels<NewTaskViewModel>()

    lateinit var nameEditText: EditText
    lateinit var descEditText: EditText

    lateinit var dateStartButton: Button
    lateinit var dateEndButton: Button
    lateinit var saveButton: Button

    lateinit var dateStartTextView: TextView
    lateinit var dateEndTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().actionBar?.setTitle(R.string.new_task_actionbar_title)


        initializeViews(view)

        addListeners()

        setObservers()
    }

    private fun initializeViews(root: View) {
        with(root) {
            nameEditText = findViewById(R.id.newTaskNameEditText)
            descEditText = findViewById(R.id.newTaskDescEditText)

            dateStartButton = findViewById(R.id.newTaskDateStartButton)
            dateEndButton = findViewById(R.id.newTaskDateEndButton)
            saveButton = findViewById(R.id.newTaskSaveButton)

            dateStartTextView = findViewById(R.id.newTaskDateStartTextView)
            dateEndTextView = findViewById(R.id.newTaskDateEndTextView)
        }
    }

    private fun addListeners() {
        dateStartButton.setOnClickListener {
            pickDateTime { dateTime -> newTaskViewModel.setNewTaskDateStart(dateTime) }
        }

        dateEndButton.setOnClickListener {
            pickDateTime { dateTime -> newTaskViewModel.setNewTaskDateEnd(dateTime) }
        }

        saveButton.setOnClickListener {
            with(newTaskViewModel) {
                setNewTaskName(nameEditText.text.toString())
                setNewTaskDescription(descEditText.text.toString())

                validateTask()
            }
        }
    }

    private fun setObservers() {
        newTaskViewModel.snackbar.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                newTaskViewModel.onSnackbarShow()
            }
        }

        newTaskViewModel.isSaveSuccessful.observe(viewLifecycleOwner) { isSaveSuccessful ->
            if (isSaveSuccessful) {
                findNavController().popBackStack()
            }
        }

        newTaskViewModel.dateStartString.observe(viewLifecycleOwner) { value ->
            dateStartTextView.text = value
        }

        newTaskViewModel.dateEndString.observe(viewLifecycleOwner) { value ->
            dateEndTextView.text = value
        }
    }

    private fun pickDateTime(setDateTime: (LocalDateTime) -> Unit) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val pickedDateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
                setDateTime(pickedDateTime)

            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }
}