package com.created.team201.presentation.createStudy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.created.team201.R
import com.created.team201.databinding.ActivityCreateStudyBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.createStudy.model.CreateStudyUiState.Fail
import com.created.team201.presentation.createStudy.model.CreateStudyUiState.Idle
import com.created.team201.presentation.createStudy.model.CreateStudyUiState.Success
import com.created.team201.presentation.createStudy.model.FragmentState.FirstFragment
import com.created.team201.presentation.createStudy.model.FragmentState.SecondFragment
import com.created.team201.presentation.createStudy.model.FragmentType
import com.created.team201.presentation.createStudy.model.FragmentType.FIRST
import com.created.team201.presentation.createStudy.model.FragmentType.SECOND
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateStudyActivity :
    BindingActivity<ActivityCreateStudyBinding>(R.layout.activity_create_study) {
    private val createStudyViewModel: CreateStudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initActionBar()
        setupCollectCreateStudyState()
        setupCollectCreateStudyUiState()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbCreateStudy)
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (createStudyViewModel.fragmentState.value) {
            is FirstFragment -> {
                finish()
                true
            }

            is SecondFragment -> {
                createStudyViewModel.navigateToBefore()
                true
            }
        }
    }

    private fun setProgressIndicator(type: FragmentType) {
        binding.lpiCreateStudyProgress.progress = when (type) {
            FIRST -> PROGRESS_FIRST
            SECOND -> PROGRESS_SECOND
        }
    }

    private fun setupCollectCreateStudyUiState() {
        lifecycleScope.launch {
            createStudyViewModel.createStudyUiState.collectLatest { createStudyUiState ->
                when (createStudyUiState) {
                    is Success -> {
                        // TODO 어디로 갈건데?
                        showToast(R.string.create_study_toast_create_study_success)
                        finish()
                    }

                    is Fail -> {
                        showToast(R.string.create_study_toast_create_study_fail)
                        finish()
                    }

                    is Idle -> throw IllegalArgumentException()
                }
            }
        }
    }

    private fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
    }

    private fun setupCollectCreateStudyState() {
        lifecycleScope.launch {
            createStudyViewModel.fragmentState.collect { fragmentState ->
                showFragment(fragmentState.type)
            }
        }
    }

    private fun showFragment(type: FragmentType) {
        setProgressIndicator(type)

        val (fragment: Fragment, isCreated: Boolean) = findFragment(
            type
        ) ?: createFragment(type)

        supportFragmentManager.commit {
            setReorderingAllowed(true)

            if (isCreated) add(R.id.fcv_create_study, fragment, type.name)

            hideAllFragment()
            show(fragment)
        }
    }

    private fun createFragment(type: FragmentType): Pair<Fragment, Boolean> {
        val fragment = when (type) {
            FIRST -> FirstCreateStudyFragment()
            SECOND -> SecondCreateStudyFragment()
        }
        return Pair(fragment, true)
    }

    private fun findFragment(type: FragmentType): Pair<Fragment, Boolean>? {
        val fragment = supportFragmentManager.findFragmentByTag(type.name) ?: return null
        return Pair(fragment, false)
    }

    private fun hideAllFragment() {
        supportFragmentManager.fragments.forEach { fragment ->
            supportFragmentManager.commit {
                hide(fragment)
            }
        }
    }

    companion object {
        private const val PROGRESS_FIRST = 33
        private const val PROGRESS_SECOND = 66

        fun getIntent(context: Context): Intent =
            Intent(context, CreateStudyActivity::class.java)
    }
}
