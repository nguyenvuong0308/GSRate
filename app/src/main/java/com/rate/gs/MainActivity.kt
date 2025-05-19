package com.rate.gs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.core.rate.RateInApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linear = LinearLayoutCompat(this)
        val button = Button(this)
        button.text = "Feedback"
        button.setOnClickListener {
            RateInApp.instance.showDialogRateAndFeedback(context = this, withOutAnimation = true)
        }

        val button2 = Button(this)
        button2.text = "NewActivity"
        button2.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        linear.addView(button)
        linear.addView(button2)
        setContentView(linear)


    }
}