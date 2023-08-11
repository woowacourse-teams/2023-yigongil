package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.home.HomeClickListener
import com.created.team201.presentation.home.adapter.viewholder.OptionalToDoViewHolder
import com.created.team201.presentation.home.model.TodoWithRoundIdUiModel

class OptionalToDoAdapter(
    private val onClick: HomeClickListener,
) : ListAdapter<TodoWithRoundIdUiModel, OptionalToDoViewHolder>(diffCallBack) {

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
        holder.bind(getItem(position))
    }


    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<TodoWithRoundIdUiModel>() {
            override fun areItemsTheSame(
                oldItem: TodoWithRoundIdUiModel,
                newItem: TodoWithRoundIdUiModel
            ): Boolean {
                return oldItem.todoId == newItem.todoId
            }

            override fun areContentsTheSame(
                oldItem: TodoWithRoundIdUiModel,
                newItem: TodoWithRoundIdUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
