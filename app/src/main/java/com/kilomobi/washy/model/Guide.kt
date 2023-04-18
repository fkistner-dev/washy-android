package com.kilomobi.washy.model

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.viewholder.GuideDetailViewHolder
import com.kilomobi.washy.viewholder.GuideListViewHolder
import java.io.Serializable

data class Guide(
    @Exclude var reference: String = "",
    var author: String = "",
    var authorPicture: String = "",
    var title: String = "",
    var description: String = "",
    var photo: String = "",
    var innerGuide: HashMap<String, Any> = HashMap(),
    var like: Int = 0,
    var isPromotional: Boolean = false,
    var verified: Boolean = false,
    var steps: Int = 0, // number of inner steps to achieve the guide, useful to mix with innerGuide[step]
    @Transient var createdAt: Timestamp = Timestamp.now(),
    @Transient var expireAt: Timestamp = Timestamp.now()
) : RecyclerItem(), AdapterClick, Serializable

data class InnerGuide (
    var prerequisite: String = "",
    var header: String = "",
    var text: String = "",
    var warning: String = "",
    var photo: String = ""
): RecyclerItem(), Serializable

object GuideListCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Guide
    }

    override fun type(): Int {
        return R.layout.row_guide_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return GuideListViewHolder(parent.viewOf(type()))
    }

    override fun bind(
        holder: RecyclerView.ViewHolder,
        item: RecyclerItem?,
        selectedPosition: Int,
        listener: AdapterListener?
    ) {
        if (holder is GuideListViewHolder && item is Guide) {
            holder.bind(item, selectedPosition)
            holder.itemView.findViewById<CardView>(holder.cardview)?.setOnClickListener {
                listener?.listen(item, holder)
            }
        }
    }
}

object GuideDetailCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is InnerGuide
    }

    override fun type(): Int {
        return R.layout.layout_guide_detail_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return GuideDetailViewHolder(parent.viewOf(type()))
    }

    override fun bind(
        holder: RecyclerView.ViewHolder,
        item: RecyclerItem?,
        selectedPosition: Int,
        listener: AdapterListener?
    ) {
        if (holder is GuideDetailViewHolder && item is InnerGuide) {
            holder.bind(item, selectedPosition)
        }
    }
}

open class GuideListAdapter(listener: AdapterListener) : BaseListAdapter(
    GuideListCell,
    listener = listener
)

open class GuideDetailAdapter(listener: AdapterListener) : BaseListAdapter(
    GuideDetailCell,
    listener = listener
)