package com.created.team201.presentation.myPage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.created.team201.R
import com.created.team201.databinding.FragmentMyPageBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.presentation.setting.SettingActivity

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val myPageViewModel: MyPageViewModel by viewModels {
        MyPageViewModel.Factory
    }

    override fun onResume() {
        super.onResume()

        myPageViewModel.updateProfile()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setActionBar()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = myPageViewModel
    }

    private fun setActionBar() {
        binding.tbMyPage.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_my_page_setting -> {
                    // 설정 뷰 이동
                    startActivity(SettingActivity.getIntent(requireContext()))
                    true
                }

                else -> false
            }
        }
    }
}
