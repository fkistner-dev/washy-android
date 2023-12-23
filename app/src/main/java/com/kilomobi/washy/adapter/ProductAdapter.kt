/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.adapter

import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.viewholder.ProductCell

open class ProductAdapter(listener: AdapterListener) : BaseListAdapter(
    ProductCell,
    listener = listener
) {
    var selectedItemPosition: Int = -1
}