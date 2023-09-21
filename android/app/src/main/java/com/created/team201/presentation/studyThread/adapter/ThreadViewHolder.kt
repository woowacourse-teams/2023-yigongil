//package com.created.team201.presentation.studyThread.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.created.team201.databinding.ItemThreadCommentBinding
//import com.created.team201.presentation.studyThread.model.ThreadComment
//
//class ThreadViewHolder(
//    private val binding: ItemThreadCommentBinding
//) : RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(item: ThreadComment) {}
//
//    companion object {
//        fun from(parent: ViewGroup): ThreadViewHolder =
//            ThreadViewHolder(
//                ItemThreadCommentBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                ),
//            )
//    }
//}