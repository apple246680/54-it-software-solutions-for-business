package com.example.wml

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.example.wml.Base.BaseActivity
import com.example.wml.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.button.setOnClickListener() {
            if (binding.textView3.text == "正常") {
                if (binding.editTextText.text.isNotBlank()) {
                    Global.Acc = binding.editTextText.text.toString()
                    binding.editTextText.setText("")
                    startActivity(MainActivity2::class.java)
                }
                else{
                    Toast.makeText(binding.root.context, "請輸入使用者名稱", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Post()
    }

    fun Post() {
        Api.postStatus {
            binding.root.post {
                val code = it.getString("ResultCode")
                if (code == "0000") {
                    binding.button.setBackgroundColor(Color.GREEN)
                    binding.textView3.setTextColor(Color.GREEN)
                    binding.textView3.text = "正常"
                }
            }
        }
    }
}