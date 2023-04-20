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