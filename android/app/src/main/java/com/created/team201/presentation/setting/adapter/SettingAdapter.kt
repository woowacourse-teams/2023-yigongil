package com.created.team201.presentation.setting.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.created.team201.presentation.setting.SettingClickListener
import com.created.team201.presentation.setting.model.SettingUiModel

class SettingAdapter(
    private val settingClickListener: SettingClickListener
) : ListAdapter<SettingUiModel, SettingViewHolder>(SettingDiffUtil) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        return SettingViewHolder(parent, settingClickListener)
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long = getItem(position).id

    companion object {
        private val SettingDiffUtil = object : DiffUtil.ItemCallback<SettingUiModel>() {
            override fun areItemsTheSame(
                oldItem: SettingUiModel,
                newItem: SettingUiModel
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SettingUiModel,
                newItem: SettingUiModel
            ): Boolean =
                oldItem == newItem
        }
    }
}
