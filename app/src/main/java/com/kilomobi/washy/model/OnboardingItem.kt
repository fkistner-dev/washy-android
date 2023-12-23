/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.model

import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

data class OnboardingItem(val position: Int = -1, val image: Int? = null, val background: Int, val title: String, val description: String = "") : RecyclerItem(),
    AdapterClick {
    override fun equals(other: Any?): Boolean {
        return false
    }
}