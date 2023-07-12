package com.created.team201.presentation.home.model

import com.created.team201.R

data class Grass(
    val grassType: GrassType,
    val grassState: Boolean,
) {
    enum class GrassType(val viewId: Int) {
        TOP(R.drawable.ic_selector_grass_top),
        MIDDLE(R.drawable.ic_selector_grass_middle),
        BOTTOM(R.drawable.ic_selector_grass_bottom),
    }
}
