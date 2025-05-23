package com.medium.music.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.medium.music.constant.AboutUsConfig
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.ActivitySplashBinding
import com.medium.music.prefs.DataStoreManager
import com.medium.music.utils.StringUtil

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var mActivitySplashBinding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mActivitySplashBinding!!.root)
        initUi()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ goToNextActivity() }, 2000)
    }

    private fun initUi() {
        mActivitySplashBinding!!.tvAboutUsTitle.text = AboutUsConfig.ABOUT_US_TITLE
        mActivitySplashBinding!!.tvAboutUsSlogan.text = AboutUsConfig.ABOUT_US_SLOGAN
    }

    private fun goToNextActivity() {
        if (DataStoreManager.user != null && !StringUtil.isEmpty(DataStoreManager.user!!.email)) {
            GlobalFunction.startActivity(this, MainActivity::class.java)
        } else {
            GlobalFunction.startActivity(this, SignInActivity::class.java)
        }
        finish()
    }
}