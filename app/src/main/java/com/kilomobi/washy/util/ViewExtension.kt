package com.kilomobi.washy.util

import android.view.View
import android.widget.TextView

fun TextView.textOrHide(newText: String?) = run {
    if (newText?.isEmpty() == true) {
        visibility = View.GONE
    } else {
        text = newText
    }
}