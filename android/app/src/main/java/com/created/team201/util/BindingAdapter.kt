package com.created.team201.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("isVisible")
    fun isVisible(view: View, isVisible: Boolean) {
        when (isVisible) {
            true -> view.visibility = View.VISIBLE
            false -> view.visibility = View.INVISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("isGone")
    fun isGone(view: View, isGone: Boolean) {
        when (isGone) {
            true -> view.visibility = View.GONE
            false -> view.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("isSelected")
    fun isSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected
    }

    @JvmStatic
    @BindingAdapter("glideSrc")
    fun glideSrc(imageview: ImageView, image: Int) {
        Glide.with(imageview.context)
            .load(image)
            .into(imageview)
    }
}
