package com.created.team201.presentation.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.data.model.UserStudyEntity


class HomeAdapter(
    private val onStudyClick: (studyId: Long) -> Unit
) : ListAdapter<UserStudyEntity, HomeViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.from(parent, onStudyClick)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack =
            object : DiffUtil.ItemCallback<UserStudyEntity>() {
                override fun areItemsTheSame(
                    oldItem: UserStudyEntity,
                    newItem: UserStudyEntity
                ): Boolean {
                    return oldItem.studyId == newItem.studyId
                }

                override fun areContentsTheSame(
                    oldItem: UserStudyEntity,
                    newItem: UserStudyEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
