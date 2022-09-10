package com.kilomobi.washy.util

fun String.toListId() = trim().splitToSequence(' ').filter { it.isNotEmpty() }.toList()
