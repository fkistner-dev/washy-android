package com.kilomobi.washy.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kilomobi.washy.recycler.RecyclerItem

interface AdapterListener {
    fun listen(click: AdapterClick?)
}

interface AdapterClick

val BASE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecyclerItem>() {

    override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        return true
//        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        return oldItem == newItem
    }

}