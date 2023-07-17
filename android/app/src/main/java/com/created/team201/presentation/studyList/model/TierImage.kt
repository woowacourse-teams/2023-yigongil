package com.created.team201.presentation.studyList.model

import com.created.team201.R

enum class TierImage(private val id: Int, private val resId: Int) {
    BRONZE(1, R.drawable.ic_bronze_24),
    SILVER(2, R.drawable.ic_silver_24),
    GOLD(3, R.drawable.ic_gold_24),
    PLATINUM(4, R.drawable.ic_platinum_24),
    DIAMOND(5, R.drawable.ic_diamond_24),
    ;

    companion object {
        fun valueOf(id: Int): Int {
            return TierImage.values().find { image ->
                image.id == id
            }?.resId ?: throw IllegalAccessException("해당 티어를 찾을 수 없습니다.")
        }
    }
}