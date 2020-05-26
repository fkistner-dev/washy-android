package com.kilomobi.washy.merchant

import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.BaseListAdapter

open class MerchantAdapter(listener: AdapterListener) : BaseListAdapter(
    MerchantCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}