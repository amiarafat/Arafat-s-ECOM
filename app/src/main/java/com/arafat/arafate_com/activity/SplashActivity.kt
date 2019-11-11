package com.arafat.arafate_com.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.arafat.arafate_com.R

class SplashActivity : AppCompatActivity() {

    lateinit var ic_logo : ImageView
    private var handler: Handler? = null
    private var handler1: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ic_logo = findViewById(R.id.ic_logo)
        ic_logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_in))

        handler = Handler()
        val r  =  Runnable {
            ic_logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_out))
            handler1 = Handler()
            val r2 = Runnable {
                ic_logo.visibility = View.GONE

                val intent : Intent =  Intent(this,AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
            handler1!!.postDelayed(r2,500)
        }
        handler!!.postDelayed(r,1000)

    }
}
