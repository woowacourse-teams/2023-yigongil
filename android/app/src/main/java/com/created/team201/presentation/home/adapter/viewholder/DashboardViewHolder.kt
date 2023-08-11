package com.created.team201.presentation.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeBinding
import com.created.team201.presentation.home.HomeClickListener
import com.created.team201.presentation.home.adapter.HomeGrassAdapter
import com.created.team201.presentation.home.adapter.OptionalToDoAdapter
import com.created.team201.presentation.home.model.StudyUiModel

class DashboardViewHolder(
    onClick: HomeClickListener,
    private val binding: ItemHomeBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val optionalTodoAdapter: OptionalToDoAdapter by lazy { OptionalToDoAdapter(onClick) }
    private val homeGrassAdapter: HomeGrassAdapter by lazy { HomeGrassAdapter() }

    init {
        binding.onClick = onClick
        attachOptionalTodoAdapter()
        attachHomeGrassAdapter()
    }

    private fun attachOptionalTodoAdapter() {
        binding.rvHomeOptionalTodoList.setHasFixedSize(true)
        binding.rvHomeOptionalTodoList.itemAnimator = null
        binding.rvHomeOptionalTodoList.adapter = optionalTodoAdapter
    }

    private fun attachHomeGrassAdapter() {
        binding.rvHomeGrass.setHasFixedSize(true)
        binding.rvHomeGrass.itemAnimator = null
        binding.rvHomeGrass.adapter = homeGrassAdapter
    }

    fun bind(studyUiModel: StudyUiModel) {
        binding.studyUiModel = studyUiModel

        optionalTodoAdapter.submitList(studyUiModel.optionalTodos)
        optionalTodoAdapter.passRoundId(studyUiModel.roundId)

        homeGrassAdapter.submitList(studyUiModel.grass)
    }

    companion object {
        fun getView(parent: ViewGroup, layoutInflater: LayoutInflater): ItemHomeBinding =
            ItemHomeBinding.inflate(layoutInflater, parent, false)
    }
}
