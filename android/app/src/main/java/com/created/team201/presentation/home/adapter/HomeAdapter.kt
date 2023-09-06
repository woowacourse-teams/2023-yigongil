package com.created.team201.presentation.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.adapter.viewholder.DashboardViewHolder
import com.created.team201.presentation.home.adapter.viewholder.FeedViewHolder
import com.created.team201.presentation.home.model.HomeViewState


class HomeAdapter : ListAdapter<HomeViewState, RecyclerView.ViewHolder>(diffCallBack) {

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is HomeViewState.Feed -> FeedViewType.FEED.ordinal
            is HomeViewState.DashBoard -> FeedViewType.DASH_BOARD.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (FeedViewType.get(viewType)) {
            FeedViewType.DASH_BOARD -> DashboardViewHolder.from(parent)
            FeedViewType.FEED -> FeedViewHolder.from(parent)
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DashboardViewHolder -> holder.bind(getItem(position) as HomeViewState.DashBoard)
            is FeedViewHolder -> holder.bind(getItem(position) as HomeViewState.Feed)
        }
    }

    private enum class FeedViewType {
        DASH_BOARD, FEED;

        companion object {
            fun get(type: Int): FeedViewType = values().first { it.ordinal == type }
        }
    }

    companion object {
        private val diffCallBack =
            object : DiffUtil.ItemCallback<HomeViewState>() {
                override fun areItemsTheSame(
                    oldItem: HomeViewState,
                    newItem: HomeViewState
                ): Boolean =
                    when {
                        (oldItem is HomeViewState.Feed) and (newItem is HomeViewState.Feed) ->
                            (oldItem as HomeViewState.Feed).content == (newItem as HomeViewState.Feed).content

                        (oldItem is HomeViewState.DashBoard) and (newItem is HomeViewState.DashBoard) ->
                            (oldItem as HomeViewState.DashBoard).mustDo == (newItem as HomeViewState.DashBoard).mustDo

                        else -> false
                    }


                override fun areContentsTheSame(
                    oldItem: HomeViewState,
                    newItem: HomeViewState
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}