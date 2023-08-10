package com.created.team201.presentation.report

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityReportBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.report.model.DateUiModel
import com.created.team201.presentation.report.model.ReportCategory
import java.util.Calendar

class ReportActivity : BindingActivity<ActivityReportBinding>(R.layout.activity_report) {

    private val reportViewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initToolBar()
        initDatePickerClickListener()
        initKeyboardSetting()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = reportViewModel
    }

    private fun initToolBar() {
        setSupportActionBar(binding.tbReport)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            setHomeActionContentDescription(R.string.toolbar_back_text)
        }
    }

    private fun initDatePickerClickListener() {
        binding.ivReportCalenderButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val todayDate = DateUiModel(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + MONTH_CALIBRATION_VALUE,
            cal.get(Calendar.DAY_OF_MONTH),
        )
        val selectedDate = reportViewModel.selectedDate.value ?: todayDate

        DatePickerDialog(
            this,
            reportViewModel.updateDate(),
            selectedDate.year,
            selectedDate.month,
            selectedDate.day,
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    private fun initKeyboardSetting() {
        binding.clReport.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
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

    companion object {
        private const val MONTH_CALIBRATION_VALUE = 1
        private const val KEY_CATEGORY = "key_category"
        private const val KEY_TARGET_ID = "key_target_id"
        fun getIntent(context: Context, reportCategory: ReportCategory, targetId: Long): Intent =
            Intent(context, ReportActivity::class.java).apply {
                putExtra(KEY_CATEGORY, reportCategory.index)
                putExtra(KEY_TARGET_ID, targetId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
