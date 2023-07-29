package com.created.team201.presentation.splash

import android.content.Intent
import android.os.Bundle
import com.created.team201.R
import com.created.team201.databinding.ActivitySplashBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.util.CustomTabLauncherActivity

class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, CustomTabLauncherActivity::class.java))
        finish()
    }
}
