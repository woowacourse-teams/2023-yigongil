package com.created.team201.presentation.home

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView.OVER_SCROLL_NEVER
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.created.team201.databinding.FragmentHomeBinding
import kotlin.math.abs
import kotlin.math.max

class CustomViewPager(
    private val binding: FragmentHomeBinding,
    context: Context,
) {
    private val displayWidth = context.resources.displayMetrics.widthPixels
    private val pageWidth = DISPLAY_RATIO * displayWidth
    private val pagePadding = ((displayWidth - pageWidth) / 2).toInt()
    private val innerPadding = pagePadding / 40

    fun setCustomViewPager() {
        setPagesSize()
        setScrollMode()
        setOffPageLimit()
    }

    private fun setPagesSize() {
        binding.vpHome.setPadding(pagePadding, 0, pagePadding, 0)
        binding.vpHome.setPageTransformer(
            CompositePageTransformer().apply {
                addTransformer(ZoomOutPageTransformer())
                addTransformer { page, position ->
                    page.translationX = position * -(innerPadding)
                }
            },
        )
    }

    private fun setScrollMode() {
        binding.vpHome.getChildAt(FIRST).overScrollMode = OVER_SCROLL_NEVER
    }

    private fun setOffPageLimit() {
        binding.vpHome.offscreenPageLimit = 1
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height

                when {
                    position < -1 -> initializeAlpha(0f)
                    position <= 1 -> setScale(position, pageHeight, pageWidth)
                    else -> initializeAlpha(0f)
                }
            }
        }

        private fun View.setScale(
            position: Float,
            pageHeight: Int,
            pageWidth: Int,
        ) {
            val scaleFactor = max(MIN_SCALE, 1 - abs(position))
            val verticalMargin = pageHeight * (1 - scaleFactor) / 2
            val horizontalMargin = pageWidth * (1 - scaleFactor) / 2

            translationX =
                if (position < 0) horizontalMargin - verticalMargin / 2 else horizontalMargin + verticalMargin / 2
            scaleX = scaleFactor
            scaleY = scaleFactor

            initializeAlpha((MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA))))
        }

        private fun View.initializeAlpha(f: Float) {
            alpha = f
        }
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
        private const val FIRST = 0
        private const val DEFAULT_WIDTH = 360.0
        private const val DISPLAY_RATIO = 256 / DEFAULT_WIDTH
    }
}
