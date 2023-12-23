/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.adapter

import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.viewholder.OnboardingCell

open class OnboardingAdapter(listener: AdapterListener) : BaseListAdapter(
    OnboardingCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}