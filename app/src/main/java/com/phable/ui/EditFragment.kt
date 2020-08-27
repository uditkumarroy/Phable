package com.phable.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.basicmvi.ui.state.MainStateEvent
import com.google.android.material.snackbar.Snackbar
import com.phable.R
import com.phable.databinding.FragmentEditBinding
import com.phable.models.Task
import com.phable.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class EditFragment : Fragment(R.layout.fragment_edit) {

    private val TAG = "Task_Fragment"
    private val viewModel:MainVewModel by viewModels()
    private lateinit var binding:FragmentEditBinding
    private var task:Task? = null
    private lateinit var mView:View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        binding = FragmentEditBinding.bind(view)
        if (arguments?.getInt("id")!=null){
            viewModel.taskStateEvent(MainStateEvent.GetTaskEvent(arguments?.getInt("id")!!))
        }
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.dataTaskState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Sucess<Long> -> {
                    displayProgressBar(false)
                    Log.e(TAG, "" + dataState.data)
                    if (dataState.data != null) {
                        view?.snack("Saved")
                    }
                }
                is DataState.Error -> {
                    displayError(dataState.exception.message.toString())
                    displayProgressBar(false)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })

        viewModel.dataGetTaskState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Sucess<Task> -> {
                    displayProgressBar(false)
                    Log.e(TAG, "" + dataState.data.name)
                    if (dataState.data != null) {
                        binding.taskData = dataState.data
                        task = dataState.data
                    }
                }
                is DataState.Error -> {
                    displayError(dataState.exception.message.toString())
                    displayProgressBar(false)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
        viewModel.dataUpdateTaskState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Sucess<Int> -> {
                    displayProgressBar(false)
                    Log.e(TAG, "" + dataState.data)
                    if (dataState.data != null) {
                        view?.snack("Updated")
                    }
                }
                is DataState.Error -> {
                    displayError(dataState.exception.message.toString())
                    displayProgressBar(false)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        btn_save.setOnClickListener {
            checkData()
        }
    }

    private fun displayError(message: String) {
        if (null != message) {
            view?.snack(message)
        } else {
            view?.snack("Unknown Error")
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        progressBarEdit.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }

    private fun checkData() {
        val tsLong:Int  = Math.random().roundToInt()
        if (null==arguments?.getInt("id")){
            task = Task(tsLong ,et_name.text.toString(),et_email.text.toString())
            viewModel.taskStateEvent(MainStateEvent.CreateTaskEvent(task!!))
        }else{
            task = task?.id?.let { Task(it,et_name.text.toString(),et_email.text.toString()) }
            viewModel.taskStateEvent(MainStateEvent.UpdateTaskEvent(task!!))
        }
        task = null
        mView.findNavController().navigateUp()
    }


}