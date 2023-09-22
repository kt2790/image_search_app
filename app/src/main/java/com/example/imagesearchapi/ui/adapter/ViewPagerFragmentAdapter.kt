package com.example.imagesearchapi.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imagesearchapi.ui.fragment.BookmarkFragment
import com.example.imagesearchapi.ui.fragment.SearchFragment

class ViewPagerFragmentAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter (fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(SearchFragment())
        fragmentList.add(BookmarkFragment())
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}