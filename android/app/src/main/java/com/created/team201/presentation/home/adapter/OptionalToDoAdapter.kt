package com.created.team201.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.adapter.viewholder.OptionalToDoViewHolder

class OptionalToDoAdapter : RecyclerView.Adapter<OptionalToDoViewHolder>() {
    private val items: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionalToDoViewHolder {
        return OptionalToDoViewHolder(
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
    fun updateToDoItems(todoItems: List<String>) {
        items.clear()
        items.addAll(todoItems)

        notifyDataSetChanged()
    }
}
