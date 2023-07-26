package com.created.team201.presentation.studyManagement

import android.os.Bundle
import android.view.MenuItem
import com.created.team201.R
import com.created.team201.databinding.ActivityStudyManagementBinding
import com.created.team201.presentation.common.BindingActivity

class StudyManagementActivity :
    BindingActivity<ActivityStudyManagementBinding>(R.layout.activity_study_management) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbStudyManagement)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
