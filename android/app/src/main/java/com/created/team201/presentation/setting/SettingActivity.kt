package com.created.team201.presentation.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.created.team201.R
import com.created.team201.databinding.ActivitySettingBinding
import com.created.team201.presentation.accountSetting.AccountSettingActivity
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.setting.adapter.SettingAdapter
import com.created.team201.presentation.setting.model.SettingType
import com.created.team201.presentation.setting.model.SettingUiModel

class SettingActivity : BindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingItems: List<SettingUiModel> by lazy {
        resources.getStringArray(R.array.settingItems).mapIndexed { index, setting ->
            SettingUiModel(index.toLong(), setting)
        }
    }

    private val onSettingItemClick: SettingClickListener by lazy {
        object : SettingClickListener {
            override fun onClick(itemId: Long) {
                when (SettingType.valueOf(itemId)) {
                    SettingType.NOTIFICATION -> {}
                    SettingType.ACCOUNT -> {
                        navigateToAccountSetting()
                    }

                    SettingType.POLICY -> {}
                    SettingType.LOGOUT -> {}
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()
        initSettingRecyclerView()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbSetting)
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initSettingRecyclerView() {
        binding.rvSetting.hasFixedSize()

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider_recyclerview_line)?.let {
            decoration.setDrawable(it)
        }

        binding.rvSetting.addItemDecoration(decoration)
        binding.rvSetting.adapter =
            SettingAdapter(onSettingItemClick).also { it.submitList(settingItems) }
    }

    private fun navigateToAccountSetting() {
        startActivity(AccountSettingActivity.getIntent(this))
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
            Intent(context, SettingActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
