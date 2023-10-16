package com.created.team201.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivitySplashBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.login.LoginActivity
import com.created.team201.presentation.main.MainActivity
import com.created.team201.presentation.onBoarding.model.OnBoardingDoneState
import com.created.team201.presentation.splash.SplashViewModel.State.FAIL
import com.created.team201.presentation.splash.SplashViewModel.State.IDLE
import com.created.team201.presentation.splash.SplashViewModel.State.SUCCESS
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyAppVersion()
    }

    private fun verifyAppVersion() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                showInAppUpdateDialog()
            } else {
                observeLoginState()
                observeOnBoardingDoneState()
            }
        }
        appUpdateManager.appUpdateInfo.addOnFailureListener { e ->
            Log.d("App Update Manager", "$e")
            observeLoginState()
            observeOnBoardingDoneState()
        }
    }

    private fun showInAppUpdateDialog() {
        InAppUpdateDialog(
            context = this,
            getString(R.string.in_app_update_dialog_title),
            getString(R.string.in_app_update_dialog_content),
            ::navigateToPlayStore,
        ).show()
    }

    private fun observeLoginState() {
        splashViewModel.loginState.observe(this@SplashActivity) { loginState ->
            when (loginState) {
                SUCCESS -> {
                    splashViewModel.getIsOnboardingDone()
                }

                FAIL -> navigateToLogin()
                IDLE -> throw IllegalStateException()
            }
        }
    }

    private fun observeOnBoardingDoneState() {
        splashViewModel.onBoardingDoneState.observe(this@SplashActivity) { onBoardingState ->
            when (onBoardingState) {
                is OnBoardingDoneState.Success -> {
                    when (onBoardingState.isDone) {
                        true -> navigateToMain()
                        false -> navigateToLogin()
                    }
                }

                OnBoardingDoneState.FAIL -> navigateToLogin()
                OnBoardingDoneState.IDLE -> throw IllegalStateException()
            }
        }
    }

    private fun navigateToPlayStore() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URI_MARKET_FORMAT.format(packageName))))
        finish()
    }

    private fun navigateToMain() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(LoginActivity.getIntent(this))
        finish()
    }

    companion object {
        private const val URI_MARKET_FORMAT = "market://details?id=%s"
    }
}
