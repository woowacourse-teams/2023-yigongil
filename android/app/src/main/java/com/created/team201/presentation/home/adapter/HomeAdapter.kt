package com.created.team201.presentation.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.presentation.home.adapter.HomeAdapter.FeedViewType.DASH_BOARD
import com.created.team201.presentation.home.adapter.HomeAdapter.FeedViewType.FEED
import com.created.team201.presentation.home.adapter.HomeViewState.DashBoard
import com.created.team201.presentation.home.adapter.HomeViewState.Feed
import com.created.team201.presentation.home.adapter.viewholder.DashboardViewHolder2
import com.created.team201.presentation.home.adapter.viewholder.FeedViewHolder

class HomeAdapter : ListAdapter<HomeViewState, RecyclerView.ViewHolder>(diffCallBack) {

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is Feed -> FEED.ordinal
            is DashBoard -> DASH_BOARD.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (FeedViewType.get(viewType)) {
            DASH_BOARD -> DashboardViewHolder2.from(parent)
            FEED -> FeedViewHolder.from(parent)
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DashboardViewHolder2 -> holder.bind(getItem(position) as DashBoard)
            is FeedViewHolder -> holder.bind(getItem(position) as Feed)
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
                        (oldItem is Feed) and (newItem is Feed) ->
                            (oldItem as Feed).content == (newItem as Feed).content

                        (oldItem is DashBoard) and (newItem is DashBoard) ->
                            (oldItem as DashBoard).mustDo == (newItem as DashBoard).mustDo

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