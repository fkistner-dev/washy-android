/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.recycler

class CellTypes<T>(vararg types: Cell<T>) {

    private val cellTypes: ArrayList<Cell<T>> = ArrayList()

    init {
        types.forEach { addType(it) }
    }

    private fun addType(type: Cell<T>) {
        cellTypes.add(type)
    }

    fun of(item: T?): Cell<T> {
        for (cellType in cellTypes) {
            if (cellType.belongsTo(item)) return cellType
        }
        throw NoSuchRecyclerItemTypeException()
    }

    fun of(viewType: Int): Cell<T> {
        for (cellType in cellTypes) {
            if (cellType.type() == viewType) return cellType
        }
        throw NoSuchRecyclerViewTypeException()
    }

}