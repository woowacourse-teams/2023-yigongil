package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.home.HomeClickListener
import com.created.team201.presentation.home.adapter.viewholder.OptionalToDoViewHolder
import com.created.team201.presentation.home.model.TodoUiModel

class OptionalToDoAdapter(
    private val onClick: HomeClickListener,
) : ListAdapter<TodoUiModel, OptionalToDoViewHolder>(diffCallBack) {
    private var roundId = 0

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).todoId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionalToDoViewHolder {
        return OptionalToDoViewHolder(
            onClick,
            OptionalToDoViewHolder.getView(
                parent,
                LayoutInflater.from(parent.context),
            ),
        )
    }

    override fun onBindViewHolder(holder: OptionalToDoViewHolder, position: Int) {
        holder.bind(getItem(position), roundId)
    }

    fun passRoundId(id: Int) {
        roundId = id
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<TodoUiModel>() {
            override fun areItemsTheSame(oldItem: TodoUiModel, newItem: TodoUiModel): Boolean {
                return oldItem.todoId == newItem.todoId
            }

            override fun areContentsTheSame(oldItem: TodoUiModel, newItem: TodoUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
