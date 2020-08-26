package com.phable.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.basicmvi.ui.state.MainStateEvent
import com.google.android.material.snackbar.Snackbar
import com.phable.R
import com.phable.models.Task
import com.phable.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit.*

@AndroidEntryPoint
class EditFragment : Fragment(R.layout.fragment_edit) {

    private val TAG = "Task_Fragment"
    private val viewModel:MainVewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
        btn_save.setOnClickListener {
            viewModel.taskStateEvent(MainStateEvent.CreateTaskEvent(Task(0,et_name.text.toString(),et_email.text.toString())))
        }
    }


    class OnClickActionCall internal constructor(context: Context) {
        var context: Context
        fun onClickAddName(view: View?,task: Task) {
            println("MYDATA: ${task.name}")
        }

        init {
            this.context = context
        }
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

    public fun checkData() {
        viewModel.taskStateEvent(MainStateEvent.GetTaskListEvents())
    }


}