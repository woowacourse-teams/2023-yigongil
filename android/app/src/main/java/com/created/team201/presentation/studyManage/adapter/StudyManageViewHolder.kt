package com.created.team201.presentation.studyManage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.created.team201.R
import com.created.team201.databinding.ItemStudyManageBinding
import com.created.team201.presentation.studyList.StudyListClickListener
import com.created.team201.presentation.studyList.adapter.StudyListAdapter
import com.created.team201.presentation.studyManage.model.MyStudiesUiModel

class StudyManageViewHolder(
    private val parent: ViewGroup,
    studyListClickListener: StudyListClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_study_manage, parent, false),
) {
    private val binding = ItemStudyManageBinding.bind(itemView)
    private val studyListAdapter: StudyListAdapter by lazy {
        StudyListAdapter(studyListClickListener)
    }

    init {
        setUpStudyManageList()
    }

    private fun setUpStudyManageList() {
        val dividerItemDecoration = DividerItemDecoration(parent.context, VERTICAL)
        getDrawable(parent.context, R.drawable.divider_study_list)?.let {
            dividerItemDecoration.setDrawable(it)
        }

        binding.rvStudyManageList.apply {
            adapter = studyListAdapter
            addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
        }
    }

    fun bind(item: MyStudiesUiModel) {
        Log.d("hello", item.studySummariesUiModel.toString())
        studyListAdapter.submitList(item.studySummariesUiModel)
    }
}
