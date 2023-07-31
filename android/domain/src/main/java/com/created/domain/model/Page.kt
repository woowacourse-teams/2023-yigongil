package com.created.domain.model

data class Page(val index: Int = 0) {
    operator fun inc(): Page {
        return Page(index + 1)
    }
}
