package com.kilomobi.washy.activity

import androidx.appcompat.widget.Toolbar

interface MainActivityDelegate {
  fun setupNavDrawer(toolbar: Toolbar)
  fun closeDrawer()
  fun enableNavDrawer(enable: Boolean)
}