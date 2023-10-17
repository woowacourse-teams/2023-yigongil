package com.created.team201.presentation.myPage.model

import androidx.annotation.DrawableRes
import com.created.team201.R
import com.created.team201.presentation.studyDetail.model.Tier

class TierProgress private constructor(
    @DrawableRes val tierDrawableRes: Int,
    private val progress: Int,
) {

    fun getTierProgressTable(): List<Int> {
        val blockCount = progress / BLOCK_COUNT_PER_ONE_PERCENT
        val rowBlockCount = blockCount / ROW_COUNT

        val tierProgressTable = mutableListOf<Int>()

        if (blockCount.isEvenNumber()) {
            addBlocks(rowBlockCount, tierProgressTable)
            addBlocks(rowBlockCount, tierProgressTable)
        } else {
            addBlocks(rowBlockCount, tierProgressTable)
            addBlocks(rowBlockCount, tierProgressTable, REMAINDER)
        }

        return tierProgressTable
    }

    private fun addBlocks(
        rowBlockCount: Int,
        tierProgressTable: MutableList<Int>,
        remainder: Int = 0,
    ) {
        repeat(rowBlockCount + remainder) {
            tierProgressTable.add(tierDrawableRes)
        }
        repeat(BLOCK_COUNT_PER_ONE_ROW - rowBlockCount - remainder) {
            tierProgressTable.add(R.drawable.ic_unrank_20)
        }
    }

    private fun Int.isEvenNumber(): Boolean = this % 2 == 0

    companion object {
        private const val REMAINDER = 1
        private const val BLOCK_COUNT_PER_ONE_PERCENT = 5
        private const val ROW_COUNT = 2
        private const val BLOCK_COUNT_PER_ONE_ROW = 10

        fun of(tier: Tier, progress: Int): TierProgress {
            return when (tier) {
                Tier.BRONZE -> TierProgress(R.drawable.ic_bronze_20, progress)
                Tier.SILVER -> TierProgress(R.drawable.ic_silver_20, progress)
                Tier.GOLD -> TierProgress(R.drawable.ic_gold_20, progress)
                Tier.PLATINUM -> TierProgress(R.drawable.ic_platinum_20, progress)
                Tier.DIAMOND -> TierProgress(R.drawable.ic_diamond_20, progress)
            }
        }
    }
}
