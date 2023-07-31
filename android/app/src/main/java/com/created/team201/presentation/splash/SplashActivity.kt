package com.created.team201.presentation.splash

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import com.created.team201.R
import com.created.team201.databinding.ActivitySplashBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.util.AuthIntentFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(
            AuthIntentFactory.gitHubLogin(
                this,
                Uri.parse(""),
                object : ResultReceiver(
                    Handler(Looper.getMainLooper()),
                ) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        super.onReceiveResult(resultCode, resultData)
                        Log.d("123123444", resultData.toString())
                    }
                },
            ),
        )
        finish()
    }
}
