package com.core.rate

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Telephony.Mms.Rate
import android.view.View
import android.widget.Toast
import com.core.rate.R
import com.core.rate.feedback.OnClickCheckDoubleClick
import com.google.android.play.core.review.ReviewManagerFactory

fun View.setOnClickDontDoubleClick(timeDelay: Long = 500, onClick: (View) -> Unit) {
    this.setOnClickListener(object : OnClickCheckDoubleClick(timeDelay = timeDelay) {
        override fun onClickNoDoubleClick(view: View) {
            onClick.invoke(view)
        }
    })
}

fun Activity.rateApp(inAppReview: Boolean = false) {
    val sharedPreferences = getSharedPreferences("RateInApp", Context.MODE_PRIVATE)
    if (inAppReview && sharedPreferences.getBoolean("rate_in_app", false)) {
        val mReviewManager = ReviewManagerFactory.create(this)
        val request = mReviewManager.requestReviewFlow()
        request.addOnCompleteListener { taskInfo ->
            if (taskInfo.isSuccessful) {
                val reviewInfo = taskInfo.result
                val flow = mReviewManager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { flowTask ->
                    sharedPreferences.edit().putBoolean("rate_in_app", true).apply()
                }
            } else {
                openAppInStore()
            }
        }
    } else {
        openAppInStore()
    }
}


fun Context.openAppInStore() {
    val uri =
        Uri.parse("market://details?id=" + this.packageName)
    val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
    val webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
    try {
        startActivity(myAppLinkToMarket.apply { setPackage("com.android.vending") })
    } catch (e: ActivityNotFoundException) {
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            startActivity(webIntent)
        } catch (webException: ActivityNotFoundException) {
            // Notify the user if no browser or Play Store app is available
            Toast.makeText(this, getString(R.string.fb_common_unable_find_market), Toast.LENGTH_SHORT).show()
        }
    }
}

@Suppress("DEPRECATION")
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)
                ?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = this.isConnected
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = true
                }
            }
        }
    }
    return result
}