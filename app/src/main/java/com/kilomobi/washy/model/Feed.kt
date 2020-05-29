package com.kilomobi.washy.model

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.kilomobi.washy.R
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.viewholder.FeedViewHolder
import java.util.*

data class Feed(
    var merchantId: String = "",
    var merchantName: String = "",
    var text: String = "",
    var price: Long = 0,
    var isPromotional: Boolean = false,
    var discount: Int = 0,
    var photos: List<String> = listOf(),
    var createdAt: Timestamp = Timestamp.now(),
    var expireAt: Timestamp = Timestamp.now()
) : RecyclerItem(), AdapterClick

object FeedCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Feed
    }

    override fun type(): Int {
        return R.layout.row_feed_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return FeedViewHolder(parent.viewOf(type()))
    }

    override fun bind(
        holder: RecyclerView.ViewHolder,
        item: RecyclerItem?,
        selectedPosition: Int,
        listener: AdapterListener?
    ) {

        if (holder is FeedViewHolder && item is Feed) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }

}

open class FeedAdapter(listener: AdapterListener) : BaseListAdapter(
    FeedCell,
    listener = listener
)