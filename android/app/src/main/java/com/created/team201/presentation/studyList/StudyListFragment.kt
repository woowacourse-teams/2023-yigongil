package com.created.team201.presentation.studyList

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.created.team201.R
import com.created.team201.databinding.FragmentStudyListBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.studyList.model.StudyStatus

class StudyListFragment : BindingFragment<FragmentStudyListBinding>(R.layout.fragment_study_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
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
}
