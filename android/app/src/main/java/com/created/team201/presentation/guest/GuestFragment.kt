package com.created.team201.presentation.guest

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.created.team201.R
import com.created.team201.databinding.FragmentGuestBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.guest.bottomSheet.LoginBottomSheetFragment
import com.created.team201.presentation.guest.bottomSheet.LoginBottomSheetFragment.Companion.TAG_LOGIN_BOTTOM_SHEET
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuestFragment : BindingFragment<FragmentGuestBinding>(R.layout.fragment_guest) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.onClickViewListener = ::showLoginBottomSheetDialog
    }

    private fun showLoginBottomSheetDialog() {
        removeAllFragment()
        LoginBottomSheetFragment().show(childFragmentManager, TAG_LOGIN_BOTTOM_SHEET)
    }

    private fun removeAllFragment() {
        childFragmentManager.fragments.forEach {
            childFragmentManager.commit {
                remove(it)
            }
        }
    }
}
