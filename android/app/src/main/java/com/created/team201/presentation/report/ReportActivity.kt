package com.created.team201.presentation.report

import android.os.Bundle
import android.view.MenuItem
import com.created.team201.R
import com.created.team201.databinding.ActivityReportBinding
import com.created.team201.presentation.common.BindingActivity

class ReportActivity : BindingActivity<ActivityReportBinding>(R.layout.activity_report) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolBar()
    }

    private fun initToolBar() {
        setSupportActionBar(binding.tbReport)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            setHomeActionContentDescription(R.string.toolbar_back_text)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> {
                false
            }
        }
    }
}
