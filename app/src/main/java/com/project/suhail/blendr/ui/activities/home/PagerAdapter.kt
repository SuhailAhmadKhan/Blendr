package com.project.suhail.blendr.ui.activities.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(val fragmentList: List<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
        /*if (position == 0)
            return ChatFragment()
        if (position == 1)
            return StatusFragment()
        return CallFragment()*/
    }
}