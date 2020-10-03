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
