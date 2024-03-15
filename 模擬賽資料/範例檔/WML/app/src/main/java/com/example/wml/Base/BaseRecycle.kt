package com.example.wml.Base

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseRecycle<VB: ViewBinding,T>(
    private val list:List<T>,
    private val bindingLayoutInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind:VB.(Int) -> Unit,
): RecyclerView.Adapter<BaseRecycle<VB,T>.ViewHolder>() {

    inner class ViewHolder(val vb:VB): RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(bindingLayoutInflater(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private var selectedPosition = -1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.vb.bind(position)
    }
}