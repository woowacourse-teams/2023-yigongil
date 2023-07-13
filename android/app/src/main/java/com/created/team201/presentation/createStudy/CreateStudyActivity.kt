package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityCreateStudyBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.createStudy.bottomSheet.PeopleCountBottomSheetFragment

class CreateStudyActivity :
    BindingActivity<ActivityCreateStudyBinding>(R.layout.activity_create_study) {

    private val viewModel: CreateStudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        initViewModel()
        initActionBar()
    }

    private fun initViewModel() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbCreateStudy)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    fun onIconTextButtonClick(tag: String) {
        supportFragmentManager.findFragmentByTag(tag)?.let { return }

        when (tag) {
            getString(R.string.createStudy_tag_people_count) -> {
                PeopleCountBottomSheetFragment().show(
                    supportFragmentManager,
                    tag,
                )
            }

            getString(R.string.createStudy_tag_start_date) -> {
            }

            getString(R.string.createStudy_tag_period) -> {
            }

            getString(R.string.createStudy_tag_cycle) -> {
            }
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
