package com.created.team201.presentation.chat

import com.created.team201.R
import com.created.team201.databinding.FragmentChatBinding
import com.created.team201.presentation.common.BindingFragment
import com.created.team201.util.FirebaseLogUtil
import com.created.team201.util.FirebaseLogUtil.SCREEN_CHAT

class ChatFragment : BindingFragment<FragmentChatBinding>(R.layout.fragment_chat) {
    override fun onResume() {
        super.onResume()

        FirebaseLogUtil.logScreenEvent(SCREEN_CHAT, this@ChatFragment.javaClass.simpleName)
    }
}
