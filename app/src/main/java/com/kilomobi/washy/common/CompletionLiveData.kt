/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.common

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class CompletionLiveData : LiveData<Resource<Boolean?>?>(),
    OnCompleteListener<Void?> {
    override fun onComplete(@NonNull task: Task<Void?>) {
        value = if (task.isSuccessful) {
            Resource(true)
        } else {
            Resource(task.exception)
        }
    }
}
