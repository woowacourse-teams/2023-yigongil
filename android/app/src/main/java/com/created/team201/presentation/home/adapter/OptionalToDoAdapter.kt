package com.created.team201.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.HomeClickListener
import com.created.team201.presentation.home.adapter.viewholder.OptionalToDoViewHolder
import com.created.team201.presentation.home.model.TodoUiModel

class OptionalToDoAdapter(
    private val onClick: HomeClickListener,
) : RecyclerView.Adapter<OptionalToDoViewHolder>() {
    private val items: MutableList<TodoUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionalToDoViewHolder {
        return OptionalToDoViewHolder(
            onClick,
            OptionalToDoViewHolder.getView(
                parent,
                LayoutInflater.from(parent.context),
            ),
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OptionalToDoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateToDoItems(todoItems: List<TodoUiModel>) {
        items.clear()
        items.addAll(todoItems)

        notifyDataSetChanged()
    }
}
