package com.created.team201.presentation.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeDashBoardBinding
import com.created.team201.presentation.home.adapter.HomeViewState.DashBoard

class DashboardViewHolder2(
    private val binding: ItemHomeDashBoardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dashBoard: DashBoard) {
        binding.item = dashBoard
    }

    companion object {
        fun from(parent: ViewGroup): DashboardViewHolder2 {
            val binding = ItemHomeDashBoardBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return DashboardViewHolder2(binding)
        }
    }
}
