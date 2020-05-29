package com.kilomobi.washy.adapter

import com.kilomobi.washy.viewholder.MerchantCell
import com.kilomobi.washy.recycler.BaseListAdapter

open class MerchantAdapter(listener: AdapterListener) : BaseListAdapter(
    MerchantCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}