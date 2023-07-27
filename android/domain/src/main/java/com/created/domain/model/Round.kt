package com.created.domain.model

data class Round(val id: Long, val number: Int) {
    init {
        validateOverMinValue()
    }

    private fun validateOverMinValue() {
        require(number > MIN_VALUE)
    }

    companion object {
        private const val MIN_VALUE = 0
    }
}
