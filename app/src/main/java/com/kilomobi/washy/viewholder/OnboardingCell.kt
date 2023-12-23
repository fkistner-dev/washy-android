/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.model.OnboardingItem
import com.kilomobi.washy.R
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.recycler.RecyclerItem

object OnboardingCell : Cell<RecyclerItem>() {

    var itemState = -1

    override fun belongsTo(item: RecyclerItem?): Boolean {
        if (item !is OnboardingItem) return false
        itemState = item.position
        return true
    }

    override fun type(): Int {
        return when (itemState) {
            1 -> R.layout.item_container_onboarding
            2 -> R.layout.item_container_onboarding_2
            else -> R.layout.item_container_onboarding_2
        }
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