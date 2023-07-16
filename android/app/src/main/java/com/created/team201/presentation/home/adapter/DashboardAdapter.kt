package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.home.HomeClickListener
import com.created.team201.presentation.home.adapter.viewholder.DashboardViewHolder
import com.created.team201.presentation.home.model.StudyUiModel

class DashboardAdapter(
    private val onClick: HomeClickListener,
) : ListAdapter<StudyUiModel, DashboardViewHolder>(diffCallBack) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).studyId.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(
            onClick,
            DashboardViewHolder.getView(parent, LayoutInflater.from(parent.context)),
        )
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<StudyUiModel>() {
            override fun areItemsTheSame(oldItem: StudyUiModel, newItem: StudyUiModel): Boolean {
                return oldItem.studyId == newItem.studyId
            }

            override fun areContentsTheSame(oldItem: StudyUiModel, newItem: StudyUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
