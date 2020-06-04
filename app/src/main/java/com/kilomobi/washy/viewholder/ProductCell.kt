package com.kilomobi.washy.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.model.Product
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.recycler.RecyclerItem

object ProductCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Product
    }

    override fun type(): Int {
        return R.layout.row_product_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ProductViewHolder(
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
        if (holder is ProductViewHolder && item is Product) {
            holder.bind(item, selectedPosition)
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }
}