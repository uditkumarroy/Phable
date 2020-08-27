package com.phable.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basicmvi.ui.state.MainStateEvent
import com.google.android.material.snackbar.Snackbar
import com.phable.R
import com.phable.models.Task
import com.phable.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_item_list),TaskRecyclerViewAdapter.Interaction,NoticeDialogFragment.NoticeDialogListener{

    private val TAG = "Task_Fragment"
    private val viewModel:MainVewModel by viewModels()
    private lateinit var taskRecyclerViewAdapter: TaskRecyclerViewAdapter
    private lateinit var mView: View
    private var task: Task? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        floatingActionButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_taskFragment_to_editFragment)
        }
        initRecyclerView()
        subscribeObserver()
        checkData()
    }

    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Sucess<List<Task>> -> {
                    displayProgressBar(false)
                    Log.e(TAG, "" + dataState.data)
                    if (dataState.data != null) {
                        taskRecyclerViewAdapter.submitList(
                            taskList = dataState.data,
                            isQueryExhausted = true
                        )
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

        viewModel.dataDeleteTaskState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Sucess<String> -> {
                    displayProgressBar(false)
                    Log.e(TAG, "" + dataState.data)
                    if (dataState.data != null) {
                        viewModel.taskStateEvent(MainStateEvent.GetTaskListEvents())
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

    private fun initRecyclerView() {
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
            taskRecyclerViewAdapter = TaskRecyclerViewAdapter(this@TaskFragment)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == taskRecyclerViewAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "Fragment: attempting to load next page...")
                    }
                }
            })
            adapter = taskRecyclerViewAdapter
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
        progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }

    private fun checkData() {
        viewModel.taskStateEvent(MainStateEvent.GetTaskListEvents())
    }

    override fun onItemSelected(position: Int, item: Task) {
        task = item
        showNoticeDialog(task)
    }

    override fun onDialogUpdateClick(view: View) {

    }

    override fun onDialogDeleteClick() {
        viewModel.taskStateEvent(MainStateEvent.GetTaskListEvents())
    }

    fun showNoticeDialog(t: Task?) {
        // Create an instance of the dialog fragment and show it
        val dialog = NoticeDialogFragment(this,mView,t!!)
        activity?.supportFragmentManager?.let { dialog.show(it, "NoticeDialogFragment") }
        task = null
    }
}