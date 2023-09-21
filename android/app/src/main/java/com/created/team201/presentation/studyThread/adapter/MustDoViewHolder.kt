package com.created.team201.presentation.studyThread.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.MustDo
import com.created.team201.databinding.ItemThreadMustDoBinding

class MustDoViewHolder(
    private val binding: ItemThreadMustDoBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MustDo) {
        Log.d("1231257657564767864758453", item.toString())
        binding.item = item
    }

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