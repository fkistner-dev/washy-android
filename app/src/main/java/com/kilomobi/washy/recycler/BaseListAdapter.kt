package com.kilomobi.washy.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.merchant.MerchantAdapter
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.BASE_DIFF_CALLBACK

abstract class BaseListAdapter(
    vararg types: Cell<RecyclerItem>,
    private val listener: AdapterListener? = null
) : androidx.recyclerview.widget.ListAdapter<RecyclerItem, RecyclerView.ViewHolder>(
    BASE_DIFF_CALLBACK
) {

    private val cellTypes: CellTypes<RecyclerItem> =
        CellTypes(*types)

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return cellTypes.of(item).type()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return cellTypes.of(viewType).holder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        cellTypes.of(item).bind(holder, item, if (this is MerchantAdapter) this.selectedItemPosition else -1, listener)
    }
}