package com.created.team201.presentation.createStudy

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityCreateStudyBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.createStudy.bottomSheet.PeopleCountBottomSheetFragment
import com.created.team201.presentation.createStudy.bottomSheet.PeriodBottomSheetFragment
import com.created.team201.presentation.createStudy.bottomSheet.StartDateBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateStudyActivity :
    BindingActivity<ActivityCreateStudyBinding>(R.layout.activity_create_study) {

    private val viewModel: CreateStudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        initViewModel()
        initActionBar()
        setCreateButtonListener()
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

    private fun setCreateButtonListener() {
        binding.tvCreateStudyBtnCreate.setOnClickListener {
            finish()
        }
    }

    fun onIconTextButtonClick(tag: String) {
        supportFragmentManager.findFragmentByTag(tag)?.let { return }
        createBottomSheetFragment(tag)?.show(supportFragmentManager, tag)
    }

    private fun createBottomSheetFragment(tag: String): BottomSheetDialogFragment? {
        return when (tag) {
            getString(R.string.createStudy_tag_people_count) -> {
                PeopleCountBottomSheetFragment()
            }

            getString(R.string.createStudy_tag_start_date) -> {
                StartDateBottomSheetFragment()
            }

            getString(R.string.createStudy_tag_period) -> {
                PeriodBottomSheetFragment()
            }

            getString(R.string.createStudy_tag_cycle) -> {
                null
            }

            else -> {
                null
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
