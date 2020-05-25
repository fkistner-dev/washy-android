package com.kilomobi.washy.dealer

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.db.dealer.Dealer
import com.kilomobi.washy.recycler.Cell

object DealerCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Dealer
    }

    override fun type(): Int {
        return R.layout.row_dealer_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return DealerViewHolder(
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
        if (holder is DealerViewHolder && item is Dealer) {
            holder.bind(item, selectedPosition)
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }
}