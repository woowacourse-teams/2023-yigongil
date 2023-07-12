package com.created.team201.presentation.home.model

data class Grass(
    val grassType: GrassType,
    val grassState: Boolean,
) {
    enum class GrassType {
        TOP, MIDDLE, BOTTOM,
    }
}
