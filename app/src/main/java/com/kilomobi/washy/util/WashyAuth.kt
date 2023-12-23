/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.util

import java.util.UUID

object WashyAuth {
    private var uid: String? = null

    fun getUid(): String? {
        return uid
    }

    fun generateUid(): String {
        uid = UUID.randomUUID().toString()
        return uid as String
    }

    fun resetUid() {
        uid = null
    }
}