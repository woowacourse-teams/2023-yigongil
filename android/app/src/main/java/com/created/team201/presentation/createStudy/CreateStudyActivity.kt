package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.MenuItem
import com.created.team201.R
import com.created.team201.databinding.ActivityCreateStudyBinding
import com.created.team201.presentation.common.BindingActivity

class CreateStudyActivity :
    BindingActivity<ActivityCreateStudyBinding>(R.layout.activity_create_study) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActionBar()
    }

    private fun setActionBar() {
        setSupportActionBar(binding.tbCreateStudy)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
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
