package com.created.team201.presentation.studyList.uiModel

import androidx.annotation.ColorRes
import com.created.team201.R

enum class Tier(@ColorRes val color: Int) {
    BRONZE(R.color.bronze_FBBA8B),
    SILVER(R.color.silver_CFD5DE),
    GOLD(R.color.gold_F3E199),
    PLATINUM(R.color.platinum_71FFDD),
    DIAMOND(R.color.diamond_9ABFF6),
    ;

    companion object {
        fun of(tier: Int): Tier = when (tier) {
            1 -> BRONZE
            2 -> SILVER
            3 -> GOLD
            4 -> PLATINUM
            5 -> DIAMOND
            else -> {
                throw IllegalArgumentException("잘못된 tier입니다.")
            }
        }

        fun of(tier: String): Tier = when (tier) {
            "bronze" -> BRONZE
            "silver" -> SILVER
            "gold" -> GOLD
            "platinum" -> PLATINUM
            "diamond" -> DIAMOND
            else -> {
                throw IllegalArgumentException("잘못된 tier입니다.")
            }
        }
    }
}
