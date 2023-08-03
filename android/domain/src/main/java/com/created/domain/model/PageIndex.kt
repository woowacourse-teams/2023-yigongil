package com.created.domain.model

data class PageIndex(
    val number: Int,
) {
    fun increase(maxPage: Int): PageIndex =
        PageIndex(number.coerceAtMost(maxPage))

    fun decrease(): PageIndex =
        PageIndex(number.coerceAtLeast(MIN_VIEW_PAGER_INDEX))

    companion object {
        private const val MIN_VIEW_PAGER_INDEX = 0
    }
}
