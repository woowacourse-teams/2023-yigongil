package com.created.team201.presentation.certification

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.created.team201.R
import com.created.team201.databinding.ActivityCertificationBinding
import com.created.team201.presentation.certification.model.CertificationUiState
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.util.BindingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CertificationActivity :
    BindingActivity<ActivityCertificationBinding>(R.layout.activity_certification) {

    private val certificationViewModel: CertificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupCloseButtonListener()
        setupGalleryButtonListener()
        observeUiState()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.etCertificationBody.clearFocus()
        certificationViewModel.updateContent(binding.etCertificationBody.text.toString())

        return super.dispatchTouchEvent(ev)
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
    }

    private fun setupCloseButtonListener() {
        binding.ivCertificationCloseButton.setOnClickListener {
            finish()
        }
    }

    private fun setupGalleryButtonListener() {
        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                val inputStream = contentResolver.openInputStream(uri!!)
                inputStream?.close()
                updateImageUrl(uri)
            }

        binding.ivCertificationGalleryButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    private fun updateImageUrl(uri: Uri) {
        certificationViewModel.updateImage(uri.toString())
        BindingAdapter.glideSrcUrl(binding.ivCertificationPhoto, uri.toString())
    }

    private fun observeUiState() {
        certificationViewModel.uiState.observe(this) { state ->
            binding.tvCertificationPostButton.isEnabled = state is CertificationUiState.Complete
        }
    }

    companion object {
        private const val KEY_STUDY_ID = "STUDY_ID"
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, CertificationActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
