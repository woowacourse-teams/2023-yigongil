package com.created.team201.presentation.certification

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.created.team201.R
import com.created.team201.databinding.ActivityCertificationBinding
import com.created.team201.presentation.certification.image.toAdjustImageFile
import com.created.team201.presentation.certification.model.CertificationUiState
import com.created.team201.presentation.common.BindingActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CertificationActivity :
    BindingActivity<ActivityCertificationBinding>(R.layout.activity_certification) {

    private val certificationViewModel: CertificationViewModel by viewModels()

    private var imageUri: Uri? = null
    private val cameraLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (!isSuccess) return@registerForActivityResult
            updateImageUrl(isSuccess and (imageUri != null))
        }
    private val galleryLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            updateLoadedGalleryImage(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupCloseButtonListener()
        setupGalleryButtonListener()
        setupCameraButtonListener()
        setupPostButtonListener()
        observeUiState()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        onHideKeyBoard()

        return super.dispatchTouchEvent(ev)
    }

    private fun onHideKeyBoard() {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.etCertificationBody.clearFocus()
        certificationViewModel.updateContent(binding.etCertificationBody.text.toString())
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
        binding.ivCertificationGalleryButton.setOnClickListener {
            galleryLauncher.launch(PATH_GALLERY_INPUT)
        }
    }

    private fun setupCameraButtonListener() {
        binding.ivCertificationCameraButton.setOnClickListener {
            if (checkCameraPermission()) {
                requestCameraPermission()
            } else {
                val photoFile = File.createTempFile(
                    PATH_CACHE_IMAGE_PREFIX,
                    PATH_CACHE_IMAGE_SUFFIX,
                    this.cacheDir,
                )
                imageUri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
                cameraLauncher.launch(imageUri)
            }
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CODE_CAMERA,
        )
    }

    private fun checkCameraPermission(): Boolean {
        val cameraPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA,
        )
        return cameraPermissionCheck != PackageManager.PERMISSION_GRANTED
    }

    private fun updateLoadedGalleryImage(uri: Uri?) {
        if (uri == null) return
        imageUri = uri
        val inputStream = contentResolver.openInputStream(imageUri!!)
        inputStream?.close()
        updateImageUrl(imageUri != null)
    }

    private fun updateImageUrl(isLoaded: Boolean) {
        if (!isLoaded) {
            showFailLoadingImageToast()
            return
        }
        certificationViewModel.updateImage(imageUri.toString())
        binding.ivCertificationPhoto.setImageURI(imageUri)
        imageUri = null
    }

    private fun setupPostButtonListener() {
        binding.tvCertificationPostButton.setOnClickListener {
            val studyId = intent.getLongExtra(KEY_STUDY_ID, KEY_NOT_FOUND_STUDY_ID)
            if (studyId == KEY_NOT_FOUND_STUDY_ID) {
                showFailPostToast()
                finish()
                return@setOnClickListener
            }
            imageUri?.toAdjustImageFile(this)?.let { file ->
                certificationViewModel.updateCertification(file, studyId)
            }
        }
    }

    private fun observeUiState() {
        certificationViewModel.uiState.observe(this) { state ->
            when (state) {
                is CertificationUiState.Failure -> showFailPostToast()
                is CertificationUiState.Success -> {
                    showSuccessPostToast()
                    finish()
                }

                else -> {
                    binding.tvCertificationPostButton.isEnabled = state is CertificationUiState.Complete
                }
            }
        }
    }

    private fun showFailLoadingImageToast() {
        Toast.makeText(
            this,
            R.string.certification_post_fail_loading_image,
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun showFailPostToast() {
        Toast.makeText(
            this,
            R.string.certification_post_fail_posting,
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun showSuccessPostToast() {
        Toast.makeText(
            this,
            R.string.certification_post_success_posting,
            Toast.LENGTH_SHORT,
        ).show()
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 1000
        private const val PATH_CACHE_IMAGE_PREFIX = "IMG_"
        private const val PATH_CACHE_IMAGE_SUFFIX = ".jpg"
        private const val PATH_GALLERY_INPUT = "image/*"
        private const val KEY_STUDY_ID = "STUDY_ID"
        private const val KEY_NOT_FOUND_STUDY_ID = -1L
        fun getIntent(context: Context, studyId: Long): Intent =
            Intent(context, CertificationActivity::class.java).apply {
                putExtra(KEY_STUDY_ID, studyId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}
