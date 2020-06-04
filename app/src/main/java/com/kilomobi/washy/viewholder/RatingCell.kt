package com.kilomobi.washy.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.recycler.RecyclerItem

object RatingCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Rating
    }

    override fun type(): Int {
        return R.layout.row_rating_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return RatingViewHolder(
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
        if (holder is RatingViewHolder && item is Rating) {
            holder.bind(item, selectedPosition)
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }
}