package com.kilomobi.washy

import androidx.appcompat.widget.Toolbar

interface MainActivityDelegate {
  fun setupNavDrawer(toolbar: Toolbar)

  fun enableNavDrawer(enable: Boolean)
}