package com.created.domain

data class Page(val index: Int = 0) {
    operator fun inc(): Page {
        return Page(index + 1)
    }
}
