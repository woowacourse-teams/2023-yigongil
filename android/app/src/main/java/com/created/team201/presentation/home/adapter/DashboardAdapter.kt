package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.model.HomeUiModel

class DashboardAdapter : RecyclerView.Adapter<DashboardViewHolder>() {
    private val items: MutableList<HomeUiModel> = mutableListOf(HomeUiModel("123", "123"))

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

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HomeUiModel>() {
            override fun areItemsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean {
                return oldItem.nickname == newItem.nickname
            }

            override fun areContentsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
