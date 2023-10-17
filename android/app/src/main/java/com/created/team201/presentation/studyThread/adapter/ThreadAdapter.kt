package com.created.team201.presentation.studyThread.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.domain.model.Feeds

class ThreadAdapter(private val onUserClick: (Long) -> Unit) :
    ListAdapter<Feeds, ThreadViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        return ThreadViewHolder.from(parent, onUserClick)
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack =
            object : DiffUtil.ItemCallback<Feeds>() {
                override fun areItemsTheSame(
                    oldItem: Feeds,
                    newItem: Feeds,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Feeds,
                    newItem: Feeds,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
