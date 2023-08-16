package com.created.team201.presentation.report

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.created.team201.R
import com.created.team201.databinding.ActivityReportBinding
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.report.model.DateUiModel
import com.created.team201.presentation.report.model.ReportCategory
import java.util.Calendar

class ReportActivity : BindingActivity<ActivityReportBinding>(R.layout.activity_report) {

    private val reportViewModel: ReportViewModel by viewModels { ReportViewModel.Factory }

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
        binding.activity = this
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
        val selectedDate = reportViewModel.selectedDate.value

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

    fun report() {
        val targetId = intent.getLongExtra(KEY_TARGET_ID, NON_EXISTENCE_ID)
        when (ReportCategory.valueOf(intent.getIntExtra(KEY_CATEGORY, NON_EXISTENCE_CATEGORY))) {
            ReportCategory.STUDY -> reportViewModel.reportStudy(targetId) {
                showToast(R.string.report_report_successful_done)
                finish()
            }

            ReportCategory.USER -> reportViewModel.reportUser(targetId) {
                showToast(R.string.report_report_successful_done)
                finish()
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

    private fun showToast(@StringRes stringRes: Int) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val MONTH_CALIBRATION_VALUE = 1
        private const val KEY_CATEGORY = "key_category"
        private const val KEY_TARGET_ID = "key_target_id"
        private const val NON_EXISTENCE_CATEGORY = 0
        private const val NON_EXISTENCE_ID = 0L

        fun getIntent(context: Context, reportCategory: ReportCategory, targetId: Long): Intent =
            Intent(context, ReportActivity::class.java).apply {
                putExtra(KEY_CATEGORY, reportCategory.index)
                putExtra(KEY_TARGET_ID, targetId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
