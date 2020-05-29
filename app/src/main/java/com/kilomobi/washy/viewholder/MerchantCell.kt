package com.kilomobi.washy.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.recycler.Cell

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
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }
}