/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.util

import android.text.Spanned
import android.view.View
import android.widget.TextView

fun TextView.textOrHide(newText: CharSequence?) = run {
    if (newText?.isEmpty() == true) {
        visibility = View.GONE
    } else {
        text = newText
    }
}