package com.kilomobi.washy.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.model.OnboardingItem
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.recycler.RecyclerItem

object OnboardingCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is OnboardingItem
    }

    override fun type(): Int {
        return R.layout.item_container_onboarding
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return OnboardingViewHolder(
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
        if (holder is OnboardingViewHolder && item is OnboardingItem) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }
}