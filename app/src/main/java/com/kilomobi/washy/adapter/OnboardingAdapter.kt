package com.kilomobi.washy.adapter

import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.viewholder.OnboardingCell

open class OnboardingAdapter(listener: AdapterListener) : BaseListAdapter(
    OnboardingCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}