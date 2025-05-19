package com.core.rate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.core.rate.databinding.FbDialogRateBinding


class RateDialog(context: Context) : AlertDialog(context) {
    private var mViewBinding = FbDialogRateBinding.inflate(LayoutInflater.from(context))

    var onRate: ((star: Int) -> Unit)? = null
    var onBack: (() -> Unit)? = null
    var oldImage : Int = R.drawable.fb_ic_smile_1
    var onIgnore: (() -> Unit)? = null
    private var isRated = false

    private var listStar = listOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        mViewBinding.run {
            listStar = listOf(
                ivStar1,
                ivStar2,
                ivStar3,
                ivStar4,
                ivStar5
            )
            listStar.forEachIndexed { index, imageView ->
                imageView.tag = index
                imageView.setOnClickDontDoubleClick(500) { view ->
                    listStar.forEach {
                        it.isSelected = it.tag as Int <= view.tag as Int
                    }
                    ivReview.isEnabled = true
                    val smileIcon = getSmileIcon(view.tag as Int)
                    if(ivSmile.isVisible) {
                        if(smileIcon != oldImage) {
                            animateIconChange(ivSmile, smileIcon)
                        }
                    } else {
                        ivSmile.visibility = View.VISIBLE
                        ivSmile.setImageResource(smileIcon)
                    }
                    tvTitle.setText(getTextTitle(view.tag as Int))
                    oldImage = smileIcon
                    ivReview.setText(getTextButton(view.tag as Int))
                }
            }
            ivReview.setOnClickListener {
                val star = listStar.last { it.isSelected }.tag as Int
                onRate?.invoke(star + 1)
                isRated = true
                dismiss()
            }

            lottieStar.addAnimatorListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    lottieStar.visibility = View.INVISIBLE
                    ivEndStar1.visibility = View.VISIBLE
                    listStar.forEach {
                        it.visibility = View.VISIBLE
                    }
                    listStar.last().startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })

            setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isShowing) {
                        dismiss()
                        onBack?.invoke()
                    }
                }
                true
            }

            setOnDismissListener {
                if(!isRated) {
                    onIgnore?.invoke()
                }
            }
        }


        val window = window
        val wlp = window?.attributes
        wlp?.let {
            wlp.width = (context.resources.displayMetrics.widthPixels * 0.9f).toInt()
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setAttributes(wlp)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }



    }

    private fun getSmileIcon(star: Int): Int {
        return when(star) {
            0 -> R.drawable.fb_ic_smile_1
            1 -> R.drawable.fb_ic_smile_2
            2 -> R.drawable.fb_ic_smile_3
            3 -> R.drawable.fb_ic_smile_4
            4 -> R.drawable.fb_ic_smile_5
            else -> R.drawable.fb_ic_smile_1
        }
    }

    private fun getTextTitle(star: Int): Int {
        return when(star) {
            0, 1, 2, 3 -> R.string.fb_rate_us_bad
            4 -> R.string.fb_rate_us_good
            else -> R.string.fb_rate_us_default
        }
    }

    private fun getTextButton(star: Int): Int {
        return when(star) {
            0, 1, 2, 3 -> R.string.fb_feedback_rate
            4 -> R.string.fb_rate_on_google_play
            else -> R.string.fb_feedback_rate
        }
    }

    private fun animateIconChange(imageView: ImageView, newIcon: Int) {
        val fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
        val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)

        fadeOut.duration = 200
        fadeIn.duration = 200

        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                imageView.setImageResource(newIcon)
                fadeIn.start()
            }
        })

        fadeOut.start()
    }

    fun show(withOutAnimation: Boolean) {
        show()
        runCatching {
            if(withOutAnimation) {
                mViewBinding.run {
                    lottieStar.visibility = View.INVISIBLE
                    ivEndStar1.visibility = View.VISIBLE
                    listStar.forEach {
                        it.visibility = View.VISIBLE
                    }
                    listStar.last().startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
                }
            }
        }
    }
}