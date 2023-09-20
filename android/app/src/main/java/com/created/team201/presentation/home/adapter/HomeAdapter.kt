package com.created.team201.presentation.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.home.uiState.UserStudyUiState


class HomeAdapter : ListAdapter<UserStudyUiState, HomeViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack =
            object : DiffUtil.ItemCallback<UserStudyUiState>() {
                override fun areItemsTheSame(
                    oldItem: UserStudyUiState,
                    newItem: UserStudyUiState
                ): Boolean {
                    return oldItem.studyId == newItem.studyId
                }

                override fun areContentsTheSame(
                    oldItem: UserStudyUiState,
                    newItem: UserStudyUiState
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

