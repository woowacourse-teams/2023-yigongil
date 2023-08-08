package com.created.team201.presentation.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.created.team201.R
import com.created.team201.databinding.ActivitySettingBinding
import com.created.team201.presentation.common.BindingActivity

class SettingActivity : BindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbSetting)
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
        fun getIntent(context: Context): Intent = Intent(context, SettingActivity::class.java)
    }
}
