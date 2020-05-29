package com.kilomobi.washy.activity

import androidx.appcompat.widget.Toolbar

interface MainActivityDelegate {
  fun setupNavDrawer(toolbar: Toolbar)

  fun enableNavDrawer(enable: Boolean)
}