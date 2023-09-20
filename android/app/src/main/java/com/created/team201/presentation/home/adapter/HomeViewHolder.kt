package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeStudyListBinding
import com.created.team201.presentation.home.uiState.UserStudyUiState

class HomeViewHolder(
    private val binding: ItemHomeStudyListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userStudy: UserStudyUiState) {
        binding.item = userStudy
    }

    companion object {
        fun from(parent: ViewGroup): HomeViewHolder =
            HomeViewHolder(
                ItemHomeStudyListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
            )
    }
}