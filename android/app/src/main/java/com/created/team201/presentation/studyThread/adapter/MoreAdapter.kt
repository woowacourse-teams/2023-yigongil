package com.created.team201.presentation.studyThread.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.created.team201.databinding.ItemThreadMoreBinding

class MoreAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val onClick: (Int) -> Unit,
) : ArrayAdapter<String>(context, resId) {

    private val items = listOf("Must Do 확인하기", "Must Do 인증하기", "내 스터디 정보")

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemThreadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.tvThreadMoreItem.text = ""

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemThreadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.tvThreadMoreItem.text = items[position]
        binding.root.setOnClickListener { onClick(position) }

        return binding.root
    }
}