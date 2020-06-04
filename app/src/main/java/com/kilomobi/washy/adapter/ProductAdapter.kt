package com.kilomobi.washy.adapter

import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.viewholder.ProductCell

open class ProductAdapter(listener: AdapterListener) : BaseListAdapter(
    ProductCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}