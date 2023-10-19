package com.created.team201.presentation.studyThread.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.Feeds
import com.created.team201.databinding.ItemThreadCommentBinding

class ThreadViewHolder(
    private val binding: ItemThreadCommentBinding,
    onUserClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onUserClick = onUserClick
    }

    fun bind(item: Feeds) {
        binding.item = item
    }

    companion object {
        fun from(parent: ViewGroup, onUserClick: (Long) -> Unit): ThreadViewHolder =
            ThreadViewHolder(
                ItemThreadCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onUserClick,
            )
    }
}
