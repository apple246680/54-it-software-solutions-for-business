package com.example.wml.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.wml.SpinnerData
import com.example.wml.TicketData
import com.example.wml.databinding.FragmentTicketBinding
import android.R
class Ticket : Fragment() {
    lateinit var binding: FragmentTicketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTicketBinding.inflate(layoutInflater)
        val ticketage= arrayListOf(
            SpinnerData("選擇票種",(0).toDouble()),
            SpinnerData("兒童",(0.5).toDouble()),
            SpinnerData("青少年",(0.7).toDouble()),
            SpinnerData("成人",(1).toDouble()),
            SpinnerData("老人",(0.6).toDouble())
        )
        val ticketType= arrayListOf(
            SpinnerData("選擇票型",(0).toDouble()),
            SpinnerData("日票",(3.5).toDouble()),
            SpinnerData("半年票",(303).toDouble()),
            SpinnerData("年票",(490.5).toDouble()),
        )
        var ticketageindex=0
        var tickettypeindex=0
        var ticketlist= arrayListOf<TicketData>()
        binding.apply {
            TicketAgeList.adapter=ArrayAdapter(root.context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,ticketage.map { x->x.Display })
            TicketTypeList.adapter=ArrayAdapter(root.context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,ticketType.map { x->x.Display })
            TicketAgeList.selected {
                ticketageindex=it
                NameEditText.isEnabled=ticketage[it].Display!="成人"&&ticketage[it].Display!="選擇票種"&&ticketType[tickettypeindex].Display!="選擇票型"
            }
            TicketTypeList.selected {
                tickettypeindex=it
                NameEditText.isEnabled=ticketage[ticketageindex].Display!="成人"&&ticketage[ticketageindex].Display!="選擇票種"&&ticketType[tickettypeindex].Display!="選擇票型"
            }
            add.setOnClickListener{
                if (ticketageindex!=0&&tickettypeindex!=0){
                    if (NameEditText.isEnabled){
                        if (NameEditText.text.isNotBlank()){
                            ticketlist.add(TicketData("${ticketType[tickettypeindex].Display}(${ticketage[ticketageindex].Display})",NameEditText.text.toString(),1,ticketType[tickettypeindex].Value*ticketage[ticketageindex].Value))
                        }
                        else{
                            Toast.makeText(root.context, "此票種需要記名", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        var match=ticketlist.firstOrNull{x->x.Name=="${ticketType[tickettypeindex].Display}(${ticketage[ticketageindex].Display})"}
                        if (match!=null){
                            match.Amount+=match.Amount/match.count
                            match.count++
                        }
                        else{
                            ticketlist.add(TicketData("${ticketType[tickettypeindex].Display}(${ticketage[ticketageindex].Display})","",1,ticketType[tickettypeindex].Value*ticketage[ticketageindex].Value))
                        }
                    }
                }
                else{
                    Toast.makeText(root.context, "請選則票的內容", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val data=ticketlist.map { x->
                    var text = "${x.Name}，"
                    if (x.Fullname.isBlank()) {
                        text += "${x.count}張，共"
                    } else {
                        text += "全名:${x.Fullname}，"
                    }
                    text += "${x.Amount}元"
                    text
                }
                listview.adapter=ArrayAdapter(root.context,R.layout.simple_list_item_1,data)
                AmountTextView.text="本次訂單試算共${ticketlist.sumOf { x->x.Amount }}元"
            }
            var listindex=-1
            listview.setOnItemClickListener { parent, view, position, id ->
                listindex=position
            }
            del.setOnClickListener{
                if (ticketlist.count()==0){
                    Toast.makeText(root.context, "沒有任何項目可以刪除", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (listindex==-1){
                    Toast.makeText(root.context, "請選擇項目", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                ticketlist.removeAt(listindex)
                val data=ticketlist.map { x->
                    var text = "${x.Name}，"
                    if (x.Fullname.isBlank()) {
                        text += "${x.count}張，共"
                    } else {
                        text += "全名:${x.Fullname}，"
                    }
                    text += "${x.Amount}元"
                    text
                }
                listview.adapter=ArrayAdapter(root.context,R.layout.simple_list_item_1,data)
                AmountTextView.text="本次訂單試算共${ticketlist.sumOf { x->x.Amount }}元"
                listindex=-1
            }
        }
        return binding.root
    }
    protected inline fun Spinner.selected(crossinline pos: (Int) -> Unit){
        onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position==0){
                    binding.NameEditText.isEnabled=false
                }
                pos(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}