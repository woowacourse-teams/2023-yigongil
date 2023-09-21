package com.created.team201.presentation.studyThread.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemThreadMustDoBinding
import com.created.team201.presentation.studyThread.model.MustDoRecord

class MustDoViewHolder(
    private val binding: ItemThreadMustDoBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MustDoRecord) {}

    companion object {
        fun from(parent: ViewGroup): MustDoViewHolder =
            MustDoViewHolder(
                ItemThreadMustDoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
            )
    }
}