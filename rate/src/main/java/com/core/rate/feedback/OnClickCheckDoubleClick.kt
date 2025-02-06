package com.core.rate.feedback

import android.os.Handler
import android.os.Looper
import android.view.View

abstract class OnClickCheckDoubleClick(canClick: Boolean = true, var timeDelay: Long = 500L) : View.OnClickListener {
    private var canClick = true


    init {
        this.canClick = canClick
    }

    override fun onClick(view: View) {
        if (canClick) {
            canClick = false
            view.isEnabled = false
            onClickNoDoubleClick(view)
            Handler(Looper.getMainLooper()).postDelayed({
                canClick = true
                try {
                    view.isEnabled = true
                }catch (ex: Exception) {
                }
            }, timeDelay)
        }
    }

    abstract fun onClickNoDoubleClick(view: View)
}