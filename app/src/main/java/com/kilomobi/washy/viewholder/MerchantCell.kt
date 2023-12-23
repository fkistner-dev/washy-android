/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewholder

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.recycler.Cell
import me.zhanghai.android.materialratingbar.MaterialRatingBar

object MerchantCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Merchant
    }

    override fun type(): Int {
        return R.layout.row_merchant_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return MerchantViewHolder(
            parent.viewOf(
                type()
            )
        )
    }

    override fun bind(
        holder: RecyclerView.ViewHolder,
        item: RecyclerItem?,
        selectedPosition: Int,
        listener: AdapterListener?
    ) {
        if (holder is MerchantViewHolder && item is Merchant) {
            holder.bind(item, selectedPosition)
            // TODO : not CPU efficiency, need to check after passing by ViewBinding
            holder.itemView.findViewById<MaterialRatingBar>(holder.rating).rating = item.avgRating
            holder.itemView.findViewById<CardView>(holder.cardview)?.setOnClickListener {
                listener?.listen(item)
            }
        }
    }
}