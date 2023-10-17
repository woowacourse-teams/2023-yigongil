package com.created.team201.presentation.studyThread.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.domain.model.MustDo

class MustDoAdapter : ListAdapter<MustDo, MustDoViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MustDoViewHolder =
        MustDoViewHolder.from(parent)

    override fun onBindViewHolder(holder: MustDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack =
            object : DiffUtil.ItemCallback<MustDo>() {
                override fun areItemsTheSame(oldItem: MustDo, newItem: MustDo): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: MustDo, newItem: MustDo): Boolean =
                    oldItem == newItem
            }
    }
}