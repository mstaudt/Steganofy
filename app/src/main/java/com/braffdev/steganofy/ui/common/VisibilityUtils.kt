package com.braffdev.steganofy.ui.common

import android.view.View

class VisibilityUtils {

    companion object {
        fun toVisibility(visible: Boolean): Int {
            if (visible) {
                return View.VISIBLE
            }

            return View.GONE
        }
    }
}