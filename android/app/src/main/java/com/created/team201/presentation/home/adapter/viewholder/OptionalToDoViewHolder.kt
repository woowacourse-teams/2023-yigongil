package com.created.team201.presentation.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeOptionalTodoBinding

class OptionalToDoViewHolder(
    private val binding: ItemHomeOptionalTodoBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: String) {
        binding.item = todoItem
    }

    companion object {
        fun getView(
            parent: ViewGroup,
            layoutInflater: LayoutInflater,
        ): ItemHomeOptionalTodoBinding =
            ItemHomeOptionalTodoBinding.inflate(layoutInflater, parent, false)
    }
}
