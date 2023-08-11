package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.home.adapter.viewholder.HomeGrassViewHolder
import com.created.team201.presentation.home.model.Grass

class HomeGrassAdapter : ListAdapter<Grass, HomeGrassViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGrassViewHolder {
        return HomeGrassViewHolder(
            HomeGrassViewHolder.getView(
                parent,
                LayoutInflater.from(parent.context),
            ),
        )
    }

    override fun onBindViewHolder(holder: HomeGrassViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<Grass>() {
            override fun areItemsTheSame(oldItem: Grass, newItem: Grass): Boolean {
                return oldItem.grassState == newItem.grassState
            }

            override fun areContentsTheSame(oldItem: Grass, newItem: Grass): Boolean {
                return oldItem == newItem
            }
        }
    }
}
