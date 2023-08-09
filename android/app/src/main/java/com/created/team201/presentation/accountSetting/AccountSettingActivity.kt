package com.created.team201.presentation.accountSetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.created.team201.R
import com.created.team201.databinding.ActivityAccountSettingBinding
import com.created.team201.presentation.common.BindingActivity

class AccountSettingActivity :
    BindingActivity<ActivityAccountSettingBinding>(R.layout.activity_account_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbAccountSetting)
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        fun getIntent(context: Context): Intent =
            Intent(context, AccountSettingActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
