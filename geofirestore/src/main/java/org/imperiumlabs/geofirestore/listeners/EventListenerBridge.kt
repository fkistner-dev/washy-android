/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package org.imperiumlabs.geofirestore.listeners

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint

// FULLY TESTED

/**
 * GeoQuery notifies listeners with this interface about documents that entered, exited, or moved within the query.
 */
class EventListenerBridge(private val listener: GeoQueryEventListener): GeoQueryDataEventListener {

    override fun onDocumentEntered(documentSnapshot: DocumentSnapshot, location: GeoPoint) {
        listener.onKeyEntered(documentSnapshot.id, location)
    }

    override fun onDocumentExited(documentSnapshot: DocumentSnapshot) {
        listener.onKeyExited(documentSnapshot.id)
    }

    override fun onDocumentMoved(documentSnapshot: DocumentSnapshot, location: GeoPoint) {
        listener.onKeyMoved(documentSnapshot.id, location)
    }

    override fun onDocumentChanged(documentSnapshot: DocumentSnapshot, location: GeoPoint) {
        //No-op
    }

    override fun onGeoQueryReady() {
        listener.onGeoQueryReady()
    }

    override fun onGeoQueryError(exception: Exception) {
        listener.onGeoQueryError(exception)
    }


    override fun equals(other: Any?): Boolean {
        if (other == null || other !is EventListenerBridge)
            return false
        return listener == other.listener
    }

    override fun hashCode(): Int {
        return listener.hashCode()
    }
}
