package com.core.rate

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.core.rate.feedback.FeedbackActivity

class RateInApp {

    companion object {
        val instance = RateInApp()
        const val TAG = "RateInApp"
    }

    private var startActivityIntent: ActivityResultLauncher<Intent> ?= null
    private var onResult: ((ActivityResult) -> Unit)? = null
    var isCanShowAppOpen = true
    var isShowThanks = false
    private var intentActivity = HashMap<Int, ActivityResultLauncher<Intent>>()

    // Call this method in onCreate() of Application
    fun registerActivityLifecycle(application: Application) {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.e(TAG, "onActivityCreated: ${activity::class.java.simpleName}")
                (activity as? ComponentActivity)?.let {
                    registerForFeedback(activity)
                }
            }

            override fun onActivityStarted(activity: Activity) {
                Log.e(TAG, "onActivityStarted: ${activity::class.java.simpleName}")
            }

            override fun onActivityResumed(activity: Activity) {
                Log.e(TAG, "onActivityResumed: ${activity::class.java.simpleName}")
                if (activity !is FeedbackActivity) {
                    isCanShowAppOpen = true
                    if (isShowThanks && activity is AppCompatActivity) {
                        ThankForFeedbackDialog().show(activity.supportFragmentManager, "thanks")
                        isShowThanks = false
                    }
                }
                (activity as? ComponentActivity)?.let {
                    startActivityIntent = intentActivity[activity.hashCode()]!!
                }
            }

            override fun onActivityPaused(activity: Activity) {
                Log.e(TAG, "onActivityPaused: ${activity::class.java.simpleName}")
            }

            override fun onActivityStopped(activity: Activity) {
                Log.e(TAG, "onActivityStopped: ${activity::class.java.simpleName}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.e(TAG, "onActivitySaveInstanceState: ${activity::class.java.simpleName}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e(TAG, "onActivityDestroyed: ${activity::class.java.simpleName}")
                (activity as? ComponentActivity)?.let {
                    intentActivity[activity.hashCode()]?.unregister()
                }
            }
        })
    }

    fun showDialogRateAndFeedback(
        context: ComponentActivity,
        onShowDialogRate: () -> Unit = {}, // Show dialog rate
        onRated: (star: Int) -> Unit = {}, // User click Rate
        onIgnoreRate: () -> Unit = {}, // Khi user không click vào rate mà click back
        alwaysIgnore: () -> Unit = {}, // Mỗi lần show dialog rate đều gọi hàm này
        inAppReview: Boolean = false, // Bật in-app review
        onShowThanks: () -> Boolean = { false }, // return true nếu muốn tự xử lý rate( show in-app review hoặc nhảy sang play store), mặc định sẽ nhảy sang play store
        forceShow: Boolean = false
    ) {
        if (!isInternetAvailable(context) && !forceShow) return
        RateDialog(context).also {
            onShowDialogRate()
            it.onRate = { star ->
                onRated(star)
                if (star == 5) {
                    context.rateApp(inAppReview = inAppReview)
                } else {
                    isCanShowAppOpen = false
                    showActivityFeedback(context = context) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            if(!onShowThanks()) {
                                isShowThanks = true
                            }
                            Log.e(TAG, "showFeedback: true")
                        } else {
                            Log.e(TAG, "showFeedback: false")
                        }
                    }
                }
            }

            it.onIgnore = {
                Log.e(TAG, "showDialogRateAndFeedback: onIgnore", )
                onIgnoreRate()
            }
        }.show()
        alwaysIgnore()
    }



    // Call this method in onCreate() of Activity
    private fun registerForFeedback(activity: ComponentActivity) {
        intentActivity[activity.hashCode()] = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onResult?.invoke(it)
        }
    }

    fun showActivityFeedback(context: Context, onResult: (ActivityResult) -> Unit) {
        this.onResult = onResult
        startActivityIntent?.launch(Intent(context, FeedbackActivity::class.java))
    }

}