package com.created.team201.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.created.team201.R
import com.created.team201.data.model.FinishedStudyEntity
import com.created.team201.databinding.ItemProfileFinishedStudyBinding
import com.created.team201.presentation.profile.model.FinishedStudyStatus
import com.created.team201.presentation.studyList.model.TierImage

class FinishedStudyViewHolder(
    val binding: ItemProfileFinishedStudyBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(finishedStudy: FinishedStudyEntity) {
        binding.ivFinishedStudyTier.setImage(TierImage.valueOf(finishedStudy.averageTier))
        binding.ivFinishedStudyTag.setImage(FinishedStudyStatus.imageOf(finishedStudy.isSucceed))
        binding.tvFinishedStudyTitle.text = finishedStudy.name
        binding.tvFinishedStudyPeopleCount.text =
            binding.root.context.getString(R.string.study_list_member_format)
                .format(finishedStudy.numberOfCurrentMembers, finishedStudy.numberOfMaximumMembers)
    }

    private fun ImageView.setImage(image: Int) {
        Glide.with(context)
            .load(image)
            .into(this)
    }

    companion object {
        fun getBinding(parent: ViewGroup): ItemProfileFinishedStudyBinding {
            val inflater = LayoutInflater.from(parent.context)
            return ItemProfileFinishedStudyBinding.inflate(inflater, parent, false)
        }
    }
}
