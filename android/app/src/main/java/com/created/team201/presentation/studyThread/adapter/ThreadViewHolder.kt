package com.created.team201.presentation.studyThread.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.Feeds
import com.created.team201.databinding.ItemThreadCommentBinding

class ThreadViewHolder(
    private val binding: ItemThreadCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Feeds) {
        Log.d("123123", item.content.toString())
        binding.item = item
    }

    companion object {
        fun from(parent: ViewGroup): ThreadViewHolder =
            ThreadViewHolder(
                ItemThreadCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
            )
    }
}