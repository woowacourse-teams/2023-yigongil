package com.created.team201.presentation.studyDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.databinding.ItemStudyDetailDeletedUserBinding

class StudyDeletedUserViewHolder(
    val binding: ItemStudyDetailDeletedUserBinding,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun getBinding(parent: ViewGroup): ItemStudyDetailDeletedUserBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemStudyDetailDeletedUserBinding.inflate(inflater, parent, false)
        }
    }
}
