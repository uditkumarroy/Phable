package com.phable.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.phable.R
import com.phable.models.Task
import com.phable.util.GenericViewHolder
import kotlinx.android.synthetic.main.task_item.view.*


class TaskRecyclerViewAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "ListAdapter"
    private val NO_MORE_RESULTS = -1
    private val TASK_ITEM = 0
    private val NO_MORE_RESULTS_TASK_MARKER = Task(
        0,
        "",
        ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem  == newItem
        }

    }
    private val differ = AsyncListDiffer(
        TaskRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )


    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).id == -1) {
            return TASK_ITEM
        }
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {

            NO_MORE_RESULTS -> {
                Log.e(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.task_item,
                        parent,
                        false
                    )
                )
            }

            TASK_ITEM -> {
                return TaskViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.task_item, parent, false),
                    interaction = interaction//,
                    //  requestManager = requestManager
                )
            }
            else -> {
                return TaskViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.task_item, parent, false),
                    interaction = interaction//,
//                    requestManager = requestManager
                )
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(taskList: List<Task>?, isQueryExhausted: Boolean) {
        val newList = taskList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_TASK_MARKER)
        differ.submitList(newList)
    }

    class TaskViewHolder
    constructor(
        itemView: View,
        //  val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Task) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            val name = item.name
            if (name.equals("null")) {
                itemView.tv_name.visibility = View.GONE
            } else {
                itemView.tv_name.text = "Name: ${item.name}"
            }
            val email = item.email
            if (email.equals("null")) {
                itemView.tv_email.visibility = View.GONE
            } else {
                itemView.tv_email.text = "Email: ${item.email}"
            }
        }
    }

    internal inner class TaskRecyclerChangeCallback(
        private val adapter: TaskRecyclerViewAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Task)
    }
}