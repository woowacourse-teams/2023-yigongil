package com.created.team201.presentation.studyList.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.created.team201.R
import com.created.team201.presentation.studyList.model.StudyStatus

object StudyListBindingAdapter {

    @JvmStatic
    @BindingAdapter("isOver")
    fun isOver(textView: TextView, status: StudyStatus) {
        textView.setTextColor(
            when (status) {
                StudyStatus.END -> ContextCompat.getColor(
                    textView.context,
                    R.color.grey02_78808B,
                )

                else -> ContextCompat.getColor(textView.context, R.color.white)
            },
        )
    }

    @JvmStatic
    @BindingAdapter("isRecruiting")
    fun isRecruiting(textView: TextView, status: StudyStatus) {
        textView.setText(
            when (status) {
                StudyStatus.RECRUITING -> R.string.study_list_processing_date
                else -> R.string.study_list_previous_date
            },
        )
    }
}
