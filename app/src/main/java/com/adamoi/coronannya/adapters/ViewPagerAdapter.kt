package com.adamoi.coronannya.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adamoi.coronannya.views.GlobalSummaryFragment
import com.adamoi.coronannya.views.IndonesiaSummaryFragment
import com.adamoi.coronannya.views.LocalSummaryFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val menuSize = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GlobalSummaryFragment()
            }
            1 -> {
             LocalSummaryFragment()
            }
            2 -> {
             IndonesiaSummaryFragment()
            }
            else -> {
                GlobalSummaryFragment()
            }
        }
    }

    override fun getItemCount() = menuSize
}