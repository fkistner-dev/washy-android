package com.kilomobi.washy.util

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

// To use PRIORITY_HIGH_ACCURACY, you must have ACCESS_FINE_LOCATION permission.
// Any other priority will require just ACCESS_COARSE_LOCATION,
// but will not guarantee a location update
@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.awaitCurrentLocation(priority: Int): Location? {
    return suspendCancellableCoroutine {
        // to use for request cancellation upon coroutine cancellation
        val cts = CancellationTokenSource()
        getCurrentLocation(priority, cts.token)
            .addOnSuccessListener { location ->
                // remember location is nullable, this happens sometimes
                // when the request expires before an update is acquired
                it.resume(location) {}
            }.addOnFailureListener {e ->
                it.resumeWithException(e)
            }

        it.invokeOnCancellation {
            cts.cancel()
        }
    }
}