package com.kilomobi.washy.util

import com.google.firebase.firestore.GeoPoint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GeoPointDeserializer : JsonDeserializer<GeoPoint> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GeoPoint {
        return if (json != null) {
            val latitude = json.asJsonObject["latitude"]?.asJsonPrimitive?.asDouble
            val longitude = json.asJsonObject["longitude"]?.asJsonPrimitive?.asDouble

            if (latitude != null && longitude != null) {
                GeoPoint(latitude, longitude)
            } else {
                GeoPoint(0.0,0.0)
            }
        } else {
            GeoPoint(0.0,0.0)
        }
    }
}