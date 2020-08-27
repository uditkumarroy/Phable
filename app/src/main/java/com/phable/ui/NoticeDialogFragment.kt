package com.phable.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.basicmvi.ui.state.MainStateEvent
import com.phable.R
import com.phable.models.Task
import kotlinx.android.synthetic.main.alert_dialog.view.*


class NoticeDialogFragment constructor(noticeDialogListener: NoticeDialogListener,val mView: View ,val task: Task) : DialogFragment() {

    private val listener: NoticeDialogListener = noticeDialogListener
    private val viewModel:MainVewModel by activityViewModels()

    interface NoticeDialogListener {
        fun onDialogUpdateClick(view: View)
        fun onDialogDeleteClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.alert_dialog, container, false)
        view.update.setOnClickListener {view->
            viewModel.taskStateEvent(MainStateEvent.GetTaskEvent(task.id))
            mView.findNavController().navigate(R.id.action_taskFragment_to_editFragment)
            listener.onDialogUpdateClick(view)
            dismiss()
        }
        view.delete.setOnClickListener {
            viewModel.taskStateEvent(MainStateEvent.DeleteTaskEvent(task))
            listener.onDialogDeleteClick()
            dismiss()
        }
        return view
    }

}