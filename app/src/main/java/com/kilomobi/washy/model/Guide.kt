package com.kilomobi.washy.model

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewholder.GuideViewHolder
import java.io.Serializable

data class Guide(
    @Exclude var reference: String = "",
    var author: String = "",
    var authorPicture: String = "",
    var header: String = "",
    var subHeader: String = "",
    var text: String = "",
    var subText: String = "",
    var like: Int = 0,
    var isPromotional: Boolean = false,
    var verified: Boolean = false,
    var photos: List<String> = listOf(),
    var steps: Int = 0, // number of steps to achieve the guide, useful to mix with photo[step]
    @Transient var createdAt: Timestamp = Timestamp.now(),
    @Transient var expireAt: Timestamp = Timestamp.now()
) : RecyclerItem(), AdapterClick, Serializable

object GuideCell : Cell<RecyclerItem>() {


    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Guide
    }

    override fun type(): Int {
        return R.layout.row_guide_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return GuideViewHolder(parent.viewOf(type()))
    }

    override fun bind(
        holder: RecyclerView.ViewHolder,
        item: RecyclerItem?,
        selectedPosition: Int,
        listener: AdapterListener?
    ) {
        if (holder is GuideViewHolder && item is Guide) {
            holder.bind(item, selectedPosition)
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }
}

open class GuideAdapter(listener: AdapterListener) : BaseListAdapter(
    GuideCell,
    listener = listener
)