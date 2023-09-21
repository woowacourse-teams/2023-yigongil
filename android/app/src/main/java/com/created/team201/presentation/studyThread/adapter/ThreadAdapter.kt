package com.created.team201.presentation.studyThread.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyThread.model.ThreadComment

class ThreadAdapter : ListAdapter<ThreadComment, ThreadViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        return ThreadViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack =
            object : DiffUtil.ItemCallback<ThreadComment>() {
                override fun areItemsTheSame(
                    oldItem: ThreadComment,
                    newItem: ThreadComment
                ): Boolean {
                    return oldItem.userId == newItem.userId
                }

                override fun areContentsTheSame(
                    oldItem: ThreadComment,
                    newItem: ThreadComment
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}