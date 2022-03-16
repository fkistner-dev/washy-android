package com.kilomobi.washy.activity

import com.google.firebase.analytics.FirebaseAnalytics

interface MainActivityDelegate {
  fun closeDrawer()
  fun enableNavDrawer(enable: Boolean)
  fun setFullscreen(enable: Boolean)
  fun getAnalytics(): FirebaseAnalytics
}