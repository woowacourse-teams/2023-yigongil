package com.created.team201.presentation.profile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.profile.model.FinishedStudyUiModel

class FinishedStudyAdapter :
    ListAdapter<FinishedStudyUiModel, FinishedStudyViewHolder>(diffCallBack) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedStudyViewHolder {
        return FinishedStudyViewHolder(FinishedStudyViewHolder.getBinding(parent))
    }

    override fun onBindViewHolder(holder: FinishedStudyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<FinishedStudyUiModel>() {
            override fun areItemsTheSame(
                oldItem: FinishedStudyUiModel,
                newItem: FinishedStudyUiModel,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: FinishedStudyUiModel,
                newItem: FinishedStudyUiModel,
            ): Boolean = oldItem == newItem
        }
    }
}
