package com.created.team201.presentation.studyList.model

import com.created.team201.R

enum class TierImage(private val id: Int, private val resId: Int) {
    BRONZE(1, R.drawable.ic_bronze_chip),
    SILVER(2, R.drawable.ic_silver_chip),
    GOLD(3, R.drawable.ic_gold_chip),
    DIAMOND(4, R.drawable.ic_diamond_chip),
    MASTER(5, R.drawable.ic_master_chip),
    ;

    companion object {
        fun valueOf(id: Int): Int {
            return TierImage.values().find { image ->
                image.id == id
            }?.resId ?: throw IllegalAccessException("해당 티어를 찾을 수 없습니다.")
        }
    }
}
