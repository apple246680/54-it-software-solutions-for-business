package com.example.wml.Fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.R
import android.widget.Toast
import com.example.wml.Base.BaseFragment
import com.example.wml.SpinnerData
import com.example.wml.Ticket
import com.example.wml.databinding.FragmentTicketBinding

class TicketFragment : BaseFragment<FragmentTicketBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.NameEditText.isEnabled = false
        val options1 = listOf(
            SpinnerData("選擇票種", (0).toDouble()),
            SpinnerData("兒童", (0.5).toDouble()),
            SpinnerData("青少年", (0.7).toDouble()),
            SpinnerData("老人", (0.6).toDouble()),
            SpinnerData("成人", (1).toDouble())
        )
        val options2 = listOf(
            SpinnerData("選擇票型", (0).toDouble()),
            SpinnerData("日票", (3.5).toDouble()),
            SpinnerData("半年票", (303).toDouble()),
            SpinnerData("年票", (490.5).toDouble())
        )
        binding.spinner.adapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            options1.map { x -> x.Display })
        binding.spinner2.adapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            options2.map { x -> x.Display })
        var spinner1Index=0
        var spinnerIndex2=0
        binding.spinner.selected {
            spinner1Index=it
            binding.NameEditText.isEnabled =
                options1[it].Display != "成人" && options1[it].Display != "選擇票種"
            if (options1[it].Display == "成人") {
                binding.NameEditText.setText("")
            }
        }
        binding.spinner2.selected {
            spinnerIndex2=it
        }
        var Tickets= ArrayList<Ticket>()
        binding.addButton.setOnClickListener(){
            if (spinner1Index==0||spinnerIndex2==0){
                Toast.makeText(binding.root.context, "請選擇票種或票型", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (options1[spinner1Index].Display=="成人"){
                var match=Tickets.firstOrNull { x->x.Name=="${options2[spinnerIndex2].Display}(${options1[spinner1Index].Display})" }
                if (match != null) {
                    match.Count++
                    match.Amount+=match.Amount/match.Count
                }else{
                    Tickets.add(Ticket("${options2[spinnerIndex2].Display}(${options1[spinner1Index].Display})","",1,options1 [spinner1Index].Value*options2[spinnerIndex2].Value))
                }
            }
            else{
                if (binding.NameEditText.text.isBlank()){
                    Toast.makeText(binding.root.context, "請輸入使用者名稱", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Tickets.add(Ticket("${options2[spinnerIndex2].Display}(${options1[spinner1Index].Display})",binding.NameEditText.text.toString(),1,options1 [spinner1Index].Value*options2[spinnerIndex2].Value))
            }
//            binding.RCY.layoutManager=GridLayoutManager(requireActivity(),1)
//            binding.RCY.adapter=BaseRecycle(Tickets,RcvaaBinding::inflate){pos->
//                var x=Tickets[pos]
//                var text="${x.Name}，"
//                if(x.FullName.isBlank()){
//                    text+="${x.Count}張，共"
//                }
//                else{
//                    text+="全名:${x.FullName}，"
//                }
//                text+="${x.Amount}元"
//                this.textView7.text=text
//                this.textView7.setOnClickListener(){
//                    this.textView7.setBackgroundColor(Color.CYAN)
//                }
//            }
            var datas = Tickets.map { x ->
                var text = "${x.Name}，"
                if (x.FullName.isBlank()) {
                    text += "${x.Count}張，共"
                } else {
                    text += "全名:${x.FullName}，"
                }
                text += "${x.Amount}元"
                text  // 這裡將text作為每次迭代的返回值
            }
            binding.RCY.adapter=ArrayAdapter(binding.root.context,R.layout.simple_list_item_1,datas)
            binding.textView8.text="本次訂單試算${Tickets.sumOf { x->x.Amount }}元"
            var pos=-1
            binding.RCY.setOnItemClickListener{_,_,position,_->
                pos=position
            }
            binding.aaa.setOnClickListener(){
                if (pos!=-1&&datas.count()!=0){
                    Tickets.removeAt(pos)
                    datas = Tickets.map { x ->
                        var text = "${x.Name}，"
                        if (x.FullName.isBlank()) {
                            text += "${x.Count}張，共"
                        } else {
                            text += "全名:${x.FullName}，"
                        }
                        text += "${x.Amount}元"
                        text
                    }
                    binding.RCY.adapter=ArrayAdapter(binding.root.context,R.layout.simple_list_item_1,datas)
                    binding.textView8.text="本次訂單試算${Tickets.sumOf { x->x.Amount }}元"
                }
            }
        }
    }
}