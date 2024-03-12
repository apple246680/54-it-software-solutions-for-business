package com.example.wml

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wml.Fragment.Info
import com.example.wml.Fragment.MyTicket
import com.example.wml.Fragment.Ticket

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments= arrayListOf(Ticket(),MyTicket(),Info())

    override fun getItemCount(): Int {
        return fragments.count()
    }
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}