package com.kilomobi.washy.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kilomobi.washy.recycler.RecyclerItem

interface AdapterListener {
    fun listen(click: AdapterClick?, holder: ViewHolder? = null)
//    fun listen(view: View?, click: AdapterClick?)
}

interface AdapterClick

val BASE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecyclerItem>() {

    override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        return oldItem == newItem
    }

}