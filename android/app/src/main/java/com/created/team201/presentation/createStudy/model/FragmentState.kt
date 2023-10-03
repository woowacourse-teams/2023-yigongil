package com.created.team201.presentation.createStudy.model

import com.created.team201.presentation.createStudy.model.FragmentType.FIRST
import com.created.team201.presentation.createStudy.model.FragmentType.SECOND

sealed interface FragmentState {
    val type: FragmentType

    object FirstFragment : FragmentState {
        override val type: FragmentType
            get() = FIRST
    }

    object SecondFragment : FragmentState {
        override val type: FragmentType
            get() = SECOND
    }
}
