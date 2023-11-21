package com.created.team201.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.FinishedStudy
import com.created.team201.R
import com.created.team201.databinding.ItemProfileFinishedStudyBinding

class FinishedStudyViewHolder(
    val binding: ItemProfileFinishedStudyBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(finishedStudy: FinishedStudy) {
        binding.finishedStudy = finishedStudy
        binding.periodStringFormat =
            binding.root.context.resources.getStringArray(R.array.periodUnitStrings).toList()
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemProfileFinishedStudyBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemProfileFinishedStudyBinding.inflate(inflater, parent, false)
        }
    }
}
