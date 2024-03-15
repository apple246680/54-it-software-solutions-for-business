package com.example.wml

import android.os.Bundle
import com.example.wml.Base.BaseActivity
import com.example.wml.databinding.ActivityMain2Binding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity2 : BaseActivity<ActivityMain2Binding>(ActivityMain2Binding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.textView5.text = "World Museum Leaague\n(登入:${Global.Acc})"
        binding.button3.setOnClickListener() {
            finish()
        }
        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewpageraa.adapter = pagerAdapter
        val pagerName = arrayListOf("票價試算", "我的票券", "關於")
        TabLayoutMediator(binding.tbb, binding.viewpageraa) { tab, position ->
            tab.text = pagerName[position]
        }.attach()
    }
}