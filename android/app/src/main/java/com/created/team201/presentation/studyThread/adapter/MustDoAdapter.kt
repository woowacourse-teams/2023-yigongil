package com.created.team201.presentation.studyThread.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.studyThread.model.MustDoRecord

class MustDoAdapter : ListAdapter<MustDoRecord, MustDoViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MustDoViewHolder {
        return MustDoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MustDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack =
            object : DiffUtil.ItemCallback<MustDoRecord>() {
                override fun areItemsTheSame(
                    oldItem: MustDoRecord,
                    newItem: MustDoRecord
                ): Boolean {
                    return oldItem.userId == newItem.userId
                }

                override fun areContentsTheSame(
                    oldItem: MustDoRecord,
                    newItem: MustDoRecord
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}