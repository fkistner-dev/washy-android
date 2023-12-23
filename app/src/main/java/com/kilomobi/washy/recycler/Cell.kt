/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.adapter.AdapterListener

abstract class Cell<T> {

    abstract fun belongsTo(item: T?): Boolean
    abstract fun type(): Int
    abstract fun holder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bind(holder: RecyclerView.ViewHolder, item: T?, selectedPosition: Int, listener: AdapterListener?)

    protected fun ViewGroup.viewOf(@LayoutRes resource: Int): View {
        return LayoutInflater
            .from(context)
            .inflate(resource, this, false)
    }
}