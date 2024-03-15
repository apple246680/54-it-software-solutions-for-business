package com.example.wml

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wml.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        status()
        binding.apply {
            StartButton.setBackgroundColor(Color.GRAY)
            StatusTextView.setTextColor(Color.YELLOW)
            StartButton.setOnClickListener{
                if(StatusTextView.text=="正常"&&UserNameEditText.text.isNotBlank()){
                    Global.UserID=UserNameEditText.text.toString()
                    UserNameEditText.setText("")
                    startActivity(Intent(binding.root.context,MainActivity2()::class.java))
                }
            }
        }
    }

    fun status() {
        binding.apply {
            StatusTextView.setTextColor(Color.YELLOW)
            StartButton.setBackgroundColor(Color.GRAY)
            StatusTextView.setText("連線中...")
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Api.postAsync("http://itwebapi54.ddns.net:5000/api/ConnectionMonitor/LoginStatus",JSONObject().put("stationNumber",20).toString())
                withContext(Dispatchers.Main){
                    binding.apply {
                        StartButton.setBackgroundColor(Color.GREEN)
                        StatusTextView.setTextColor(Color.GREEN)
                        StatusTextView.setText("正常")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.apply {
                        StartButton.setBackgroundColor(Color.GRAY)
                        StatusTextView.setTextColor(Color.RED)
                        StatusTextView.setText("無法連線至伺服器，將自動重試")
                    }
                }
                delay(1000)
                withContext(Dispatchers.Main){
                    status()
                }
            }
        }
    }
}