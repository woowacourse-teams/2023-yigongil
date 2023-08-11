package com.created.team201.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
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
        binding.bnvMain.setOnItemSelectedListener(::replaceFragment)
    }

    private fun replaceFragment(item: MenuItem): Boolean {
        when (FragmentType.valueOf(item.itemId)) {
            HOME -> replaceFragment<HomeFragment>()
            STUDY_LIST -> replaceFragment<StudyListFragment>()
            STUDY_MANAGE -> replaceFragment<StudyManageFragment>()
            CHAT -> replaceFragment<ChatFragment>()
            MY_PAGE -> replaceFragment<MyPageFragment>()
        }
        return true
    }

    private inline fun <reified T : Fragment> replaceFragment() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main)
            setReorderingAllowed(true)
        }
    }

    private enum class FragmentType(val resId: Int) {
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
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
