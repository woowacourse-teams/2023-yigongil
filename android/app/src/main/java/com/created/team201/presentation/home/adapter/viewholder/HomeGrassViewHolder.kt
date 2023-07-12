package com.created.team201.presentation.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeGrassBinding
import com.created.team201.presentation.home.model.Grass

class HomeGrassViewHolder(
    private val binding: ItemHomeGrassBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(grass: Grass) {
        binding.grass = grass
    }

    companion object {
        fun getView(parent: ViewGroup, layoutInflater: LayoutInflater): ItemHomeGrassBinding =
            ItemHomeGrassBinding.inflate(layoutInflater, parent, false)
    }
}
