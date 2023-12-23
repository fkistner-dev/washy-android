/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

fun Fragment.initToolbar(toolbar: Toolbar, backEnabled: Boolean) {
    val appCompatActivity = activity as AppCompatActivity
    appCompatActivity.setSupportActionBar(toolbar)
    appCompatActivity.supportActionBar?.title = ""
    appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(backEnabled)
}
