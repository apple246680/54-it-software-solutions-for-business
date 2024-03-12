package com.example.wml.Fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wml.Api
import com.example.wml.Global
import com.example.wml.R
import com.example.wml.databinding.FragmentInfoBinding
import com.example.wml.databinding.FragmentMyTicketBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayInputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyTicket.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyTicket : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMyTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMyTicketBinding.inflate(layoutInflater)
        binding.TicketNumberTextView.text="TICKET-01-${Global.UserID}"
        CoroutineScope(Dispatchers.IO).launch {
            val aa=Api.postAsync("http://10.0.2.2:5000/Image/GenerateQRCode",JSONObject().put("Text",binding.TicketNumberTextView.text.toString()).put("SizePixels",512).put("CorrectionLevel",3).toString())
            val bb=ByteArrayInputStream(aa).readBytes()
            withContext(Dispatchers.Main){
                binding.QRCODEImageView.setImageBitmap(BitmapFactory.decodeByteArray(bb,0,bb.size))
                object :CountDownTimer(60000,1000){
                    override fun onFinish() {
                        binding.TimeTextView.text="請更新"
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        binding.TimeTextView.text="請於00:${millisUntilFinished/1000}內使用"
                    }
                }.start()
            }
        }
        binding.REFImageVIew.setOnClickListener{
            if (binding.TimeTextView.text=="請更新"){
                CoroutineScope(Dispatchers.IO).launch {
                    val aa=Api.postAsync("http://10.0.2.2:5000/Image/GenerateQRCode",JSONObject().put("Text",binding.TicketNumberTextView.text.toString()).put("SizePixels",512).put("CorrectionLevel",3).toString())
                    val bb=ByteArrayInputStream(aa).readBytes()
                    withContext(Dispatchers.Main){
                        binding.QRCODEImageView.setImageBitmap(BitmapFactory.decodeByteArray(bb,0,bb.size))
                        object :CountDownTimer(60000,1000){
                            override fun onFinish() {
                                binding.TimeTextView.text="請更新"
                            }

                            override fun onTick(millisUntilFinished: Long) {
                                binding.TimeTextView.text="請於00:${millisUntilFinished/1000}內使用"
                            }
                        }.start()
                    }
                }
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyTicket.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyTicket().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}