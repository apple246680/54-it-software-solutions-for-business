package com.example.wml

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wml.Fragment.InfoFragment
import com.example.wml.Fragment.MyTicketFragment
import com.example.wml.Fragment.TicketFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = arrayListOf(TicketFragment(), MyTicketFragment(), InfoFragment())
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}