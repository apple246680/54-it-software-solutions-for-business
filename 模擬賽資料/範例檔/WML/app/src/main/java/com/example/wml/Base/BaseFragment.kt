package com.example.wml.Base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB: ViewBinding>(): Fragment() {
    protected lateinit var binding: VB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var type=javaClass.genericSuperclass as ParameterizedType
        var clazz=type.actualTypeArguments[0] as Class<*>
        val bindinginf=clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding=bindinginf.invoke(null,inflater) as VB
        return binding.root
    }
    protected fun ShowFragment(fragment: Fragment){
//        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.ShowPanel,fragment).addToBackStack(null).commit()
    }
    protected inline fun Spinner.selected(crossinline pos: (Int) -> Unit){
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                pos(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}