package com.kilomobi.washy.adapter

import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.viewholder.RatingCell

class RatingAdapter(listener: AdapterListener) : BaseListAdapter(
    RatingCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}