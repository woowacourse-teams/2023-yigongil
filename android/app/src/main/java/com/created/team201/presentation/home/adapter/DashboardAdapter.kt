package com.created.team201.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.HomeClickListener
import com.created.team201.presentation.home.adapter.viewholder.DashboardViewHolder
import com.created.team201.presentation.home.model.StudyUiModel

class DashboardAdapter(
    private val onClick: HomeClickListener,
) : RecyclerView.Adapter<DashboardViewHolder>() {
    private val items: MutableList<StudyUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(
            onClick,
            DashboardViewHolder.getView(
                parent,
                LayoutInflater.from(parent.context),
            ),
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(studyInfo: List<StudyUiModel>) {
        items.clear()
        items.addAll(studyInfo)

        notifyDataSetChanged()
    }
}
