package com.example.wml.Fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wml.Api
import com.example.wml.Base.BaseFragment
import com.example.wml.Global
import com.example.wml.R
import com.example.wml.databinding.FragmentMyTicketBinding
import java.text.SimpleDateFormat
import java.util.Date

class MyTicketFragment : BaseFragment<FragmentMyTicketBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView6.text =
            "TICKET-01-${Global.Acc}-${SimpleDateFormat("HH:mm:ss").format(Date(Date().time + 60 * 1000))}"
        Api.postQRCODE(binding.textView6.text.toString()) {
            binding.root.post {
                binding.imageView2.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
                object : CountDownTimer(60000,1000){
                    override fun onFinish() {
                        binding.textView10.text="請更新"
                    }
                    override fun onTick(millisUntilFinished: Long) {
                        binding.textView10.text="請於00:${millisUntilFinished/1000}秒內使用"
                    }
                }.start()
            }
        }
        binding.imageView4.setOnClickListener(){
            if (binding.textView10.text=="請更新"){
                binding.textView6.text ="TICKET-01-${Global.Acc}-${SimpleDateFormat("HH:mm:ss").format(Date(Date().time + 60 * 1000))}"
                Api.postQRCODE(binding.textView6.text.toString()) {
                    binding.root.post {
                        binding.imageView2.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
                        object : CountDownTimer(60000,1000){
                            override fun onFinish() {
                                binding.textView10.text="請更新"
                            }
                            override fun onTick(millisUntilFinished: Long) {
                                binding.textView10.text="請於00:${millisUntilFinished/1000}秒內使用"
                            }
                        }.start()
                    }
                }
            }
            else{
                Toast.makeText(binding.root.context, "again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}