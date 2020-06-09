package com.kilomobi.washy.activity

interface MainActivityDelegate {
  fun closeDrawer()
  fun enableNavDrawer(enable: Boolean)
  fun setFullscreen(enable: Boolean)
}