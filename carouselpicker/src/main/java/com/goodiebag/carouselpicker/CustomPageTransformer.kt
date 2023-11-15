package com.goodiebag.carouselpicker

import kotlin.math.abs
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.PageTransformer

/**
 * Created by pavan on 25/04/17.
 */
class CustomPageTransformer(private val mode: CarouselPicker.Mode) :
    PageTransformer {
    private var viewPager: ViewPager? = null
    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }
        view.scaleY = (1 - abs(position.toDouble())).toFloat()
        view.scaleX = (1 - abs(position.toDouble())).toFloat()
        if (mode === CarouselPicker.Mode.VERTICAL) {
            // Counteract the default slide transition
            view.translationX = view.width * -position

            //set Y position to swipe in from top
            val yPosition = position * view.height
            view.translationY = yPosition
        }
    }
}
