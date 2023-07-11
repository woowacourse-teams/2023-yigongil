package com.created.team201.presentation.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeBinding
import com.created.team201.presentation.home.adapter.OptionalToDoAdapter
import com.created.team201.presentation.home.model.HomeUiModel

class DashboardViewHolder(private val binding: ItemHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val optionalTodoAdapter: OptionalToDoAdapter by lazy { OptionalToDoAdapter() }

    init {
        binding.rvHomeOptionalTodoList.adapter = optionalTodoAdapter
        binding.rvHomeOptionalTodoList.setHasFixedSize(true)
    }

    fun bind(homeUiModel: HomeUiModel) {
        binding.homeUiModel = homeUiModel
        optionalTodoAdapter.updateToDoItems(homeUiModel.optionalTodos)
    }

    companion object {
        fun getView(parent: ViewGroup, layoutInflater: LayoutInflater): ItemHomeBinding =
            ItemHomeBinding.inflate(layoutInflater, parent, false)
    }
}
