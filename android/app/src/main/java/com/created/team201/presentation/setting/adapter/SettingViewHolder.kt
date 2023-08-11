package com.created.team201.presentation.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemSettingBinding
import com.created.team201.presentation.setting.SettingClickListener
import com.created.team201.presentation.setting.model.SettingUiModel

class SettingViewHolder(
    parent: ViewGroup,
    settingClickListener: SettingClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false),
) {
    private val binding = ItemSettingBinding.bind(itemView)

    init {
        itemView.setOnClickListener { settingClickListener.onClick(itemId) }
    }

    fun bind(setting: SettingUiModel) {
        binding.tvSettingTitle.text = setting.title
    }
}
