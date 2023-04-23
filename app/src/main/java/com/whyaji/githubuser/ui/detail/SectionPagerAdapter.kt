package com.whyaji.githubuser.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity, data: Bundle) : FragmentStateAdapter(activity) {

    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_SECTION_NUMBER, position)
            putString(FollowFragment.EXTRA_USERNAME, fragmentBundle.getString(DetailActivity.EXTRA_USERNAME).toString())
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return DetailActivity.TAB_TITLES.size
    }
}