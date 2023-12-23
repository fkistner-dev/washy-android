/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.model

import com.google.firebase.Timestamp
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

abstract class BaseOffer(
    var title: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var active: Boolean = true,
    var createdAt: Timestamp = Timestamp.now(),
    var imageUrl: String = ""
) : RecyclerItem(), AdapterClick