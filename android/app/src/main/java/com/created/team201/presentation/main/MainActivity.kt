package com.created.team201.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.created.team201.R
import com.created.team201.databinding.ActivityMainBinding
import com.created.team201.presentation.chat.ChatFragment
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.guest.GuestFragment
import com.created.team201.presentation.guest.GuestViewModel
import com.created.team201.presentation.home.HomeFragment
import com.created.team201.presentation.main.MainActivity.FragmentType.CHAT
import com.created.team201.presentation.main.MainActivity.FragmentType.GUEST
import com.created.team201.presentation.main.MainActivity.FragmentType.HOME
import com.created.team201.presentation.main.MainActivity.FragmentType.MY_PAGE
import com.created.team201.presentation.main.MainActivity.FragmentType.STUDY_LIST
import com.created.team201.presentation.main.MainActivity.FragmentType.STUDY_MANAGE
import com.created.team201.presentation.myPage.MyPageFragment
import com.created.team201.presentation.studyList.StudyListFragment
import com.created.team201.presentation.studyManage.StudyManageFragment

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.Factory
    }

    private val guestViewModel: GuestViewModel by viewModels {
        GuestViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserveGuestRefreshState()
        setBottomNavigationView()

    }

    private fun setObserveGuestRefreshState() {
        guestViewModel.refreshState.observe(this) { isRefresh ->
            if (isRefresh) {
                val itemId = binding.bnvMain.selectedItemId
                val fragmentType = FragmentType.valueOf(itemId)
                showFragment(fragmentType)
            }
        }
    }

    private fun setBottomNavigationView() {
        binding.bnvMain.setOnItemSelectedListener(::displayFragment)
        binding.bnvMain.selectedItemId = R.id.menu_home
    }

    private fun displayFragment(item: MenuItem): Boolean {
        when (FragmentType.valueOf(item.itemId)) {
            HOME -> showOriginOrGuest(HOME)
            STUDY_LIST -> showFragment(STUDY_LIST)
            STUDY_MANAGE -> showOriginOrGuest(STUDY_MANAGE)
            CHAT -> showOriginOrGuest(CHAT)
            MY_PAGE -> showOriginOrGuest(MY_PAGE)
            else -> throw IllegalStateException()
        }
        return true
    }

    private fun showOriginOrGuest(type: FragmentType) {
        when (mainViewModel.isGuest) {
            true -> showFragment(GUEST)
            false -> showFragment(type)
        }
    }

    private fun showFragment(type: FragmentType) {
        val (fragment: Fragment, isCreated: Boolean) = findFragment(type) ?: createFragment(type)

        supportFragmentManager.commit(true) {
            setReorderingAllowed(true)

            if (isCreated) add(R.id.fcv_main, fragment, type.name)

            hideAllFragment()
            show(fragment)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == ACTION_DOWN && currentFocus is EditText) {
            val inputMethodManager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            currentFocus?.clearFocus()
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun createFragment(type: FragmentType): Pair<Fragment, Boolean> {
        val fragment = when (type) {
            GUEST -> GuestFragment()
            HOME -> HomeFragment()
            STUDY_LIST -> StudyListFragment()
            STUDY_MANAGE -> StudyManageFragment()
            CHAT -> ChatFragment()
            MY_PAGE -> MyPageFragment()
        }
        return Pair(fragment, true)
    }

    private fun findFragment(type: FragmentType): Pair<Fragment, Boolean>? {
        val fragment = supportFragmentManager.findFragmentByTag(type.name) ?: return null
        return Pair(fragment, false)
    }

    private fun hideAllFragment() {
        supportFragmentManager.fragments.forEach { fragment ->
            supportFragmentManager.commit(true) {
                hide(fragment)
            }
        }
    }

    private enum class FragmentType(@IdRes private val resId: Int) {
        GUEST(-1),
        HOME(R.id.menu_home),
        STUDY_LIST(R.id.menu_study_list),
        STUDY_MANAGE(R.id.menu_study_manage),
        CHAT(R.id.menu_chat),
        MY_PAGE(R.id.menu_my_page),
        ;

        companion object {

            fun valueOf(id: Int): FragmentType = values().find { fragmentView ->
                fragmentView.resId == id
            } ?: throw IllegalArgumentException()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
