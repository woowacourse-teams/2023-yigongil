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
import com.created.team201.R
import com.created.team201.databinding.ActivityCreateStudyBinding
import com.created.team201.presentation.common.BindingViewActivity
import com.created.team201.presentation.createStudy.CreateStudyViewModel.CreateStudyState.Failure
import com.created.team201.presentation.createStudy.CreateStudyViewModel.CreateStudyState.Loading
import com.created.team201.presentation.createStudy.CreateStudyViewModel.CreateStudyState.Success
import com.created.team201.presentation.createStudy.CreateStudyViewModel.Event.CreateStudyFailure
import com.created.team201.presentation.createStudy.CreateStudyViewModel.Event.CreateStudySuccess
import com.created.team201.presentation.createStudy.CreateStudyViewModel.Event.NavigateToBefore
import com.created.team201.presentation.createStudy.CreateStudyViewModel.Event.NavigateToNext
import com.created.team201.presentation.createStudy.model.FragmentState
import com.created.team201.presentation.createStudy.model.FragmentState.FirstFragment
import com.created.team201.presentation.createStudy.model.FragmentState.SecondFragment
import com.created.team201.presentation.createStudy.model.FragmentType
import com.created.team201.presentation.createStudy.model.FragmentType.FIRST
import com.created.team201.presentation.createStudy.model.FragmentType.SECOND
import com.created.team201.presentation.studyDetail.StudyDetailActivity
import com.created.team201.util.collectLatestOnStarted
import com.created.team201.util.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStudyActivity :
    BindingViewActivity<ActivityCreateStudyBinding>(ActivityCreateStudyBinding::inflate) {
    private val createStudyViewModel: CreateStudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()
        showFragment(createStudyViewModel.fragmentState.value.type)
        collectCreateStudyState()
        collectCreateStudyEvent()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        createStudyViewModel.navigateToBefore()
        return true
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbCreateStudy)
        supportActionBar?.setHomeActionContentDescription(R.string.toolbar_back_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun setProgressIndicator(type: FragmentType) {
        binding.lpiCreateStudyProgress.progress = when (type) {
            FIRST -> PROGRESS_FIRST
            SECOND -> PROGRESS_SECOND
        }
    }

    private fun collectCreateStudyEvent() {
        createStudyViewModel.createStudyEvent.collectOnStarted(this) { event ->
            when (event) {
                CreateStudySuccess -> showToast(R.string.create_study_toast_create_study_success)
                CreateStudyFailure -> showToast(R.string.create_study_toast_create_study_fail)
                is NavigateToBefore -> navigateToBefore(event.fragmentState)
                is NavigateToNext -> navigateToNext(event.fragmentState)
            }
        }
    }

    private fun navigateToBefore(fragmentState: FragmentState) {
        when (fragmentState) {
            FirstFragment -> finish()
            SecondFragment -> showFragment(FirstFragment.type)
        }
    }

    private fun navigateToNext(fragmentState: FragmentState) {
        when (fragmentState) {
            FirstFragment -> showFragment(SecondFragment.type)
            SecondFragment -> Unit
        }
    }

    private fun collectCreateStudyState() {
        createStudyViewModel.createStudyState
            .collectLatestOnStarted(this) { createStudyState ->
                when (createStudyState) {
                    is Success -> {
                        startActivity(
                            StudyDetailActivity.getIntent(
                                this@CreateStudyActivity,
                                createStudyState.studyId
                            ),
                        )
                        finish()
                    }

                    Loading -> Unit
                    Failure -> finish()
                }
            }
    }

    private fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
    }

    private fun showFragment(type: FragmentType) {
        setProgressIndicator(type)

        val (fragment: Fragment, isCreated: Boolean) = findFragment(
            type,
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
        private const val PROGRESS_FIRST = 50
        private const val PROGRESS_SECOND = 100

        fun getIntent(context: Context): Intent =
            Intent(context, CreateStudyActivity::class.java)
    }
}
