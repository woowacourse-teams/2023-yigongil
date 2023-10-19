package com.created.team201.presentation.studyThread.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.MustDo
import com.created.team201.databinding.ItemThreadMustDoBinding

class MustDoViewHolder(
    private val binding: ItemThreadMustDoBinding,
    onMemberCertificationClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onMemberCertificationClick = onMemberCertificationClick
    }

    fun bind(item: MustDo) {
        binding.item = item
    }

    companion object {
        fun from(parent: ViewGroup, onMemberCertificationClick: (Long) -> Unit): MustDoViewHolder =
            MustDoViewHolder(
                ItemThreadMustDoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onMemberCertificationClick,
            )
    }
}
