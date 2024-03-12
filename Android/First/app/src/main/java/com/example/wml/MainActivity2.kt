package com.example.wml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import com.example.wml.databinding.ActivityMain2Binding
import com.example.wml.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logout.setOnClickListener{
            Global.UserID=""
            finish()
        }
        binding.title.text="World Museum Leaague\n(登入:${Global.UserID})"
        binding.viewpagerss.adapter=ViewPagerAdapter(this)
        val pagerName= arrayListOf("票價試算","我的票券","關於")
        TabLayoutMediator(binding.tbb,binding.viewpagerss){tab,pos->
            tab.text=pagerName[pos]
        }.attach()
    }
}