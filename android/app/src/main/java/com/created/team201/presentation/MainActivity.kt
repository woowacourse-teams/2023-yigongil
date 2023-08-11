package com.created.team201.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.created.team201.R
import com.created.team201.databinding.ActivityMainBinding
import com.created.team201.presentation.MainActivity.FragmentType.CHAT
import com.created.team201.presentation.MainActivity.FragmentType.HOME
import com.created.team201.presentation.MainActivity.FragmentType.MY_PAGE
import com.created.team201.presentation.MainActivity.FragmentType.STUDY_LIST
import com.created.team201.presentation.MainActivity.FragmentType.STUDY_MANAGE
import com.created.team201.presentation.chat.ChatFragment
import com.created.team201.presentation.common.BindingActivity
import com.created.team201.presentation.home.HomeFragment
import com.created.team201.presentation.myPage.MyPageFragment
import com.created.team201.presentation.studyList.StudyListFragment
import com.created.team201.presentation.studyManage.StudyManageFragment

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNavigationView()
    }

    private fun setBottomNavigationView() {
        binding.bnvMain.selectedItemId = R.id.menu_home
        binding.bnvMain.setOnItemSelectedListener(::displayFragment)
    }

    private fun displayFragment(item: MenuItem): Boolean {
        when (FragmentType.valueOf(item.itemId)) {
            HOME -> showFragment(HOME)
            STUDY_LIST -> showFragment(STUDY_LIST)
            STUDY_MANAGE -> showFragment(STUDY_MANAGE)
            CHAT -> showFragment(CHAT)
            MY_PAGE -> showFragment(MY_PAGE)
        }
        return true
    }

    private fun showFragment(type: FragmentType) {
        val (fragment: Fragment, isCreated: Boolean) = findFragment(type) ?: createFragment(type)

        supportFragmentManager.commit {
            setReorderingAllowed(true)

            if (isCreated) add(R.id.fcv_main, fragment, type.name)

            hideAllFragment()
            show(fragment)
        }
    }

    private fun createFragment(type: FragmentType): Pair<Fragment, Boolean> {
        val fragment = when (type) {
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
            supportFragmentManager.commit {
                hide(fragment)
            }
        }
    }


    private enum class FragmentType(@IdRes private val resId: Int) {
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
