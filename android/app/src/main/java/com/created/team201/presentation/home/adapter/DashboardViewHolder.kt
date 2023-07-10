package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeBinding
import com.created.team201.presentation.home.model.HomeUiModel

class DashboardViewHolder(private val binding: ItemHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(homeUiModel: HomeUiModel) {
        binding.homeUiModel = homeUiModel
        binding.tvHomeDate.text = "123123"
    }

    companion object {
        fun getView(parent: ViewGroup, layoutInflater: LayoutInflater): ItemHomeBinding =
            ItemHomeBinding.inflate(layoutInflater, parent, false)
    }
}
