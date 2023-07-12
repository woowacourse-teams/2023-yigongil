package com.created.team201.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.adapter.viewholder.HomeGrassViewHolder
import com.created.team201.presentation.home.model.HomeUiModel

class HomeGrassAdapter : RecyclerView.Adapter<HomeGrassViewHolder>() {
    private val items: MutableList<HomeUiModel.Grass> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGrassViewHolder {
        return HomeGrassViewHolder(
            HomeGrassViewHolder.getView(
                parent,
                LayoutInflater.from(parent.context),
            ),
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomeGrassViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateGrass(grass: List<HomeUiModel.Grass>) {
        items.clear()
        items.addAll(grass)

        notifyDataSetChanged()
    }
}
