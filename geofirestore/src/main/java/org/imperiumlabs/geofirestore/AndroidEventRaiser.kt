/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package org.imperiumlabs.geofirestore

import android.os.Handler
import android.os.Looper

// FULLY TESTED

class AndroidEventRaiser: EventRaiser {
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun raiseEvent(r: Runnable) {
        this.mainThreadHandler.post(r)
    }
}
