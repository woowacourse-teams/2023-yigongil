package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.model.HomeUiModel

class DashboardAdapter : RecyclerView.Adapter<DashboardViewHolder>() {
    private val items: MutableList<HomeUiModel> = mutableListOf(
        HomeUiModel("자두타타 스터디", 75, 11, "2023.07.07", "이 뷰 다 끝내기", listOf("123")),
        HomeUiModel("자두타타2 스터디", 75, 11, "2023.07.07", "이 뷰 다 끝내기", listOf("123")),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(
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
}
