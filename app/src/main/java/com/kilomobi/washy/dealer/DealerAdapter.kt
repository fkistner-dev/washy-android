package com.kilomobi.washy.dealer

import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.BaseListAdapter

open class DealerAdapter(listener: AdapterListener) : BaseListAdapter(
    DealerCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}