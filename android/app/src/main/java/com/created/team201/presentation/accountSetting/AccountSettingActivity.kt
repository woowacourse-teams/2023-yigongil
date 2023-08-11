package com.created.team201.presentation.accountSetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.created.team201.R
import com.created.team201.databinding.ActivityAccountSettingBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.login.LoginActivity
import com.created.team201.presentation.setting.SettingDialog
import com.created.team201.presentation.setting.SettingDialogClickListener
import com.created.team201.presentation.setting.SettingViewModel
import com.created.team201.presentation.setting.SettingViewModel.State.FAIL
import com.created.team201.presentation.setting.SettingViewModel.State.IDLE
import com.created.team201.presentation.setting.SettingViewModel.State.SUCCESS

class AccountSettingActivity :
    BindingActivity<ActivityAccountSettingBinding>(R.layout.activity_account_setting) {
    private val viewModel: SettingViewModel by viewModels {
        SettingViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()
        initBinding()
        setWithdrawAccountClick()
        setObserveWithdrawAccountResult()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbAccountSetting)
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setWithdrawAccountClick() {
        binding.tvAccountSettingWithdrawAccount.setOnClickListener {
            removeDialog()
            SettingDialog(
                getString(R.string.setting_dialog_withdraw_account_title),
                getString(R.string.setting_dialog_withdraw_account_content),
                object : SettingDialogClickListener {
                    override fun onCancelClick() {}

                    override fun onOkClick() {
                        viewModel.withdrawAccount()
                    }
                }
            ).show(supportFragmentManager, TAG_DIALOG_WITHDRAW_ACCOUNT)
        }
    }

    private fun removeDialog() {
        supportFragmentManager.findFragmentByTag(TAG_DIALOG_WITHDRAW_ACCOUNT)?.let {
            supportFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun setObserveWithdrawAccountResult() {
        viewModel.isWithdrawAccountState.observe(this) { state ->
            when (state) {
                SUCCESS -> {
                    Toast.makeText(
                        this,
                        getString(R.string.setting_toast_withdraw_account_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToLogin()
                }

                FAIL -> {
                    showToast(
                        getString(R.string.setting_dialog_toast_withdraw_account_fail)
                    )
                }

                IDLE -> throw IllegalStateException()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(
            LoginActivity.getIntent(this).also { it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        finishAffinity()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> false
        }

    companion object {
        private const val TAG_DIALOG_WITHDRAW_ACCOUNT = "TAG_DIALOG_WITHDRAW_ACCOUNT"

        fun getIntent(context: Context): Intent =
            Intent(context, AccountSettingActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
