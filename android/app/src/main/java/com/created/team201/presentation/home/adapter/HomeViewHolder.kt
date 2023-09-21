package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.UserStudy
import com.created.team201.databinding.ItemHomeStudyListBinding

class HomeViewHolder(
    private val binding: ItemHomeStudyListBinding,
    onClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.clHomeStudyListTop.setOnClickListener { onClick() }
    }

    fun bind(userStudy: UserStudy) {
        binding.item = userStudy
    }

    companion object {
        fun from(parent: ViewGroup, onClick: () -> Unit): HomeViewHolder =
            HomeViewHolder(
                ItemHomeStudyListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClick
            )
    }
}