package com.nd.ecommerce.ui.utils

import android.animation.ObjectAnimator
import android.view.View

fun View.showBottomBarAnimated(show: Boolean, duration: Long = 300) {
    if (show) {
        this.translationY = this.height.toFloat()
        this.alpha = 0f

        ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f).apply {
            this.duration = duration
            start()
        }

        this.animate().translationY(0f).setDuration(duration)
            .withStartAction { visibility = View.VISIBLE }.start()

    } else {
        ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f).apply {
            this.duration = duration
            start()
        }

        this.animate().translationY(this.height.toFloat()).setDuration(duration)
            .withEndAction { visibility = View.GONE }.start()
    }
}