package com.created.team201.presentation.guest.bottomSheet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentLoginBottomSheetBinding
import com.created.team201.presentation.common.BindingBottomSheetFragment
import com.created.team201.presentation.login.LoginViewModel
import com.created.team201.presentation.login.LoginViewModel.State.FAIL
import com.created.team201.presentation.login.LoginViewModel.State.IDLE
import com.created.team201.presentation.login.LoginViewModel.State.SUCCESS
import com.created.team201.presentation.onBoarding.OnBoardingActivity
import com.created.team201.presentation.onBoarding.model.OnBoardingDoneState
import com.created.team201.util.auth.CustomTabLauncherActivity

class LoginBottomSheetFragment :
    BindingBottomSheetFragment<FragmentLoginBottomSheetBinding>(R.layout.fragment_login_bottom_sheet) {
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        dialog?.setCanceledOnTouchOutside(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setObserveLoginState()
        setObserveOnBoardingDoneState()
        setClickEventOnLoginButton()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setObserveLoginState() {
        loginViewModel.signUpState.observe(viewLifecycleOwner) { loginState ->
            when (loginState) {
                SUCCESS -> loginViewModel.getIsOnboardingDone()
                FAIL -> Toast.makeText(
                    requireContext(),
                    getString(R.string.login_fail_message),
                    Toast.LENGTH_SHORT,
                ).show()

                IDLE -> throw IllegalStateException()
            }
        }
    }


    private fun setObserveOnBoardingDoneState() {
        loginViewModel.onBoardingDoneState.observe(this) { onBoardingDoneState ->
            when (onBoardingDoneState) {
                is OnBoardingDoneState.Success -> {
                    if (onBoardingDoneState.isDone) {
                        this.dismiss()
                        return@observe
                    }
                    navigateToOnBoarding()
                    this.dismiss()
                }

                OnBoardingDoneState.FAIL -> Toast.makeText(
                    requireContext(),
                    getString(R.string.login_onBoarding_fail_message),
                    Toast.LENGTH_SHORT,
                ).show()

                OnBoardingDoneState.IDLE -> throw IllegalStateException()
            }
        }
    }

    private fun setClickEventOnLoginButton() {
        binding.clLoginBtn.setOnClickListener {
            navigateToLoginWebView()
        }
    }

    private fun navigateToLoginWebView() {
        startActivity(CustomTabLauncherActivity.getIntent(requireContext(), getResult()))
    }

    private fun navigateToOnBoarding() {
        startActivity(OnBoardingActivity.getIntent(requireContext()))
    }

    private fun getResult() = object : ResultReceiver(Handler(Looper.getMainLooper())) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)

            val oauthToken = resultData.getString(CustomTabLauncherActivity.GIT_OAUTH_TOKEN_KEY)
                ?: throw IllegalArgumentException()

            loginViewModel.signUp(oauthToken)
        }
    }

    companion object {
        const val TAG_LOGIN_BOTTOM_SHEET = "TAG_LOGIN_BOTTOM_SHEET"
    }
}
