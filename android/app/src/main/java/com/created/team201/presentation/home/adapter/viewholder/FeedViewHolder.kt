package com.created.team201.presentation.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemHomeFeedBinding
import com.created.team201.presentation.home.model.HomeViewState


class FeedViewHolder(
    private val binding: ItemHomeFeedBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(feed: HomeViewState.Feed) {
        binding.item = feed
    }

    companion object {
        fun from(parent: ViewGroup): FeedViewHolder {
            val binding = ItemHomeFeedBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return FeedViewHolder(binding)
        }
    }
}