package com.created.team201.presentation.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.widget.Toast
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityLoginBinding
import com.created.team201.presentation.MainActivity
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.login.LoginViewModel.State.FAIL
import com.created.team201.presentation.login.LoginViewModel.State.IDLE
import com.created.team201.presentation.login.LoginViewModel.State.SUCCESS
import com.created.team201.util.CustomTabLauncherActivity
import com.created.team201.util.CustomTabLauncherActivity.Companion.GIT_OAUTH_TOKEN_KEY

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val loginViewModel by viewModels<LoginViewModel> { LoginViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeLoginState()
        setClickEventOnLoginButton()
    }

    private fun observeLoginState() {
        loginViewModel.loginState.observe(this) { loginState ->
            when (loginState) {
                SUCCESS -> navigateToMain()
                FAIL -> Toast.makeText(
                    this,
                    getString(R.string.login_fail_message),
                    Toast.LENGTH_SHORT,
                ).show()

                IDLE -> throw IllegalStateException()
            }
        }
    }

    private fun navigateToMain() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    private fun setClickEventOnLoginButton() {
        binding.clLoginBtn.setOnClickListener {
            val redirectUrl: String = loginViewModel.getRedirectUrl()

            navigateToLoginWebView(redirectUrl)
        }
    }

    private fun navigateToLoginWebView(redirectUrl: String) {
        startActivity(CustomTabLauncherActivity.getIntent(this, redirectUrl, getResult()))
        finish()
    }

    private fun getResult() = object : ResultReceiver(Handler(Looper.getMainLooper())) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)

            val oauthToken = resultData.getString(GIT_OAUTH_TOKEN_KEY)
                ?: throw IllegalArgumentException()

            loginViewModel.postLogin(oauthToken)
        }
    }
}
