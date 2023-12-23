/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package org.imperiumlabs.geofirestore

import java.util.concurrent.Executors

// FULLY TESTED

class ThreadEventRaiser: EventRaiser {

    private val executorService = Executors.newSingleThreadExecutor()

    override fun raiseEvent(r: Runnable) {
        this.executorService.submit(r)
    }
}
