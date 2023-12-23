/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.activity

import com.google.firebase.analytics.FirebaseAnalytics

interface MainActivityDelegate {
  fun closeDrawer()
  fun enableNavDrawer(enable: Boolean)
  fun setFullscreen(enable: Boolean)
  fun getAnalytics(): FirebaseAnalytics
}