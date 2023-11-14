package com.created.team201.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.data.model.UserStudyEntity
import com.created.team201.databinding.ItemHomeStudyListBinding

class HomeViewHolder(
    private val binding: ItemHomeStudyListBinding,
    private val onClick: (studyId: Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userStudy: UserStudyEntity) {
        setupView(userStudy)
    }

    private fun setupView(userStudy: UserStudyEntity) {
        with(binding) {
            tvHomeStudyListStudyName.text = userStudy.studyName
            tvHomeStudyListGrassSeedCount.text = root.context.getString(
                R.string.home_study_list_grass_count,
                userStudy.grassSeedsCount,
            )
            tvHomeStudyListDDay.text = root.context.getString(
                R.string.home_study_list_date,
                userStudy.leftDays,
            )
            ivHomeStudyListMaster.visibility = when (userStudy.isMaster) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            tvHomeStudyListMustDoContent.apply {
                text = when (userStudy.mustDo.isEmpty()) {
                    true -> {
                        setTextColor(root.context.getColor(R.color.grey06_30363D))
                        root.context.getString(R.string.hom_study_list_must_do_default_message)
                    }

                    false -> {
                        setTextColor(root.context.getColor(R.color.white))
                        userStudy.mustDo
                    }
                }
            }
            root.setOnClickListener { onClick(userStudy.studyId) }
        }
    }

    companion object {
        fun from(parent: ViewGroup, onClick: (studyId: Long) -> Unit): HomeViewHolder =
            HomeViewHolder(
                ItemHomeStudyListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onClick,
            )
    }
}
