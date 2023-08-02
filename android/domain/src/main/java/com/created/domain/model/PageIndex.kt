package com.created.domain.model

data class PageIndex(
    val number: Int = 0,
) {
    fun increase(maxPage: Int): PageIndex =
        PageIndex((number + ONE_PAGE).coerceAtMost(maxPage))

    fun decrease(): PageIndex =
        PageIndex((number - ONE_PAGE).coerceAtLeast(MIN_VIEW_PAGER_INDEX))

    fun isFirstPage(): Boolean = number == MIN_VIEW_PAGER_INDEX

    fun isLastPage(maxPage: Int): Boolean = number == (maxPage - ONE_PAGE)

    companion object {
        private const val ONE_PAGE = 1
        private const val MIN_VIEW_PAGER_INDEX = 0
    }
}
