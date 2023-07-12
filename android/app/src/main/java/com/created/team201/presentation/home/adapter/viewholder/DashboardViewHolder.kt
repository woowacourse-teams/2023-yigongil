package com.created.team201.presentation.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeBinding
import com.created.team201.presentation.home.adapter.HomeGrassAdapter
import com.created.team201.presentation.home.adapter.OptionalToDoAdapter
import com.created.team201.presentation.home.model.HomeUiModel

class DashboardViewHolder(private val binding: ItemHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val optionalTodoAdapter: OptionalToDoAdapter by lazy { OptionalToDoAdapter() }
    private val homeGrassAdapter: HomeGrassAdapter by lazy { HomeGrassAdapter() }

    init {
        attachOptionalTodoAdapter()
        attachHomeGrassAdapter()
    }

    private fun attachOptionalTodoAdapter() {
        binding.rvHomeOptionalTodoList.adapter = optionalTodoAdapter
        binding.rvHomeOptionalTodoList.setHasFixedSize(true)
    }

    private fun attachHomeGrassAdapter() {
        binding.rvHomeGrass.adapter = homeGrassAdapter
        binding.rvHomeGrass.setHasFixedSize(true)
    }

    fun bind(homeUiModel: HomeUiModel) {
        binding.homeUiModel = homeUiModel
        optionalTodoAdapter.updateToDoItems(homeUiModel.optionalTodos)
        homeGrassAdapter.updateGrass(homeUiModel.grass)
    }

    companion object {
        fun getView(parent: ViewGroup, layoutInflater: LayoutInflater): ItemHomeBinding =
            ItemHomeBinding.inflate(layoutInflater, parent, false)
    }
}
